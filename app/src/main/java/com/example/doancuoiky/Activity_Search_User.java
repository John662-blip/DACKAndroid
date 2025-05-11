package com.example.doancuoiky;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancuoiky.Adapter.PlantAdapter;
import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.Response.ProductPageResponse;
import com.example.doancuoiky.CallAPI.Response.ProductResponse;
import com.example.doancuoiky.CallAPI.Response.genera.Product;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Model.PlantModel;
import com.example.doancuoiky.Preferences.PreManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

public class Activity_Search_User extends AppCompatActivity {
    private PlantAdapter pladapter;
    List<PlantModel> plantList ;
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
    EditText searchBar;
    RecyclerView rc_item;
    ImageView btn_back;

    private int currentPage = 0;
    private final int pageSize = 8;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private String currentKey = "";
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        AnhXa();
        setContent();
        setClick();
        loadSearchResults();
        addScrollListener();
        handleSearchTextChanged();
    }
    private void AnhXa(){
        preManager = new PreManager(this);
        searchBar = findViewById(R.id.search_bar);
        rc_item = findViewById(R.id.recyclerView);
        btn_back = findViewById(R.id.back_button);
    }
    private void setContent(){
        plantList = new ArrayList<>();
        pladapter = new PlantAdapter(this, plantList);
        pladapter.setOnItemClickListener(plant -> {
            Intent intent = new Intent(this, Activity_Product_Detail.class);
            intent.putExtra("id", plant.getId());
            startActivity(intent);
        });
        rc_item.setAdapter(pladapter);
        rc_item.setLayoutManager(new GridLayoutManager(this, 2));
    }
    private void setClick(){
        btn_back.setOnClickListener(v->{
            finish();
        });
    }
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    private void handleSearchTextChanged() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Hủy gọi cũ nếu người dùng đang tiếp tục gõ
                handler.removeCallbacks(searchRunnable);

                searchRunnable = () -> {
                    currentKey = s.toString().trim();
                    resetPaging();
                    loadSearchResults();
                };

                // Gọi sau 500ms nếu người dùng không gõ nữa
                handler.postDelayed(searchRunnable, 500);
            }
        });
    }

    private void resetPaging() {
        currentPage = 0;
        isLastPage = false;
        plantList.clear();
        pladapter.notifyDataSetChanged();
    }

    private void addScrollListener() {
        rc_item.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager layoutManager = (GridLayoutManager) rc_item.getLayoutManager();
                if (!isLoading && !isLastPage && layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        loadSearchResults();
                    }
                }
            }
        });
    }

    private void loadSearchResults() {
        isLoading = true;

        apiService.getPageProductByName(currentKey, currentPage, pageSize).enqueue(new retrofit2.Callback<ProductPageResponse>() {
            @Override
            public void onResponse(Call<ProductPageResponse> call, retrofit2.Response<ProductPageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> newProducts = response.body().getBody().getContent(); // Bạn cần map sang PlantModel
                    if (newProducts.isEmpty()) {
                        isLastPage = true;
                    } else {
                        for (Product item : newProducts) {
                            plantList.add(new PlantModel(
                                    item.getId().intValue(),
                                    item.getProductName(),
                                    item.getStockCount(),
                                    formatter.format(item.getPrice()),
                                    item.getImageUrl()
                            ));
                        }
                        pladapter.notifyDataSetChanged();
                        currentPage++;
                    }
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<ProductPageResponse> call, Throwable t) {
                isLoading = false;
                t.printStackTrace();
            }
        });
    }
}
