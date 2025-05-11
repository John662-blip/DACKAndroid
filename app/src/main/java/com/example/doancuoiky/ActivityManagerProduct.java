package com.example.doancuoiky;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancuoiky.Adapter.ProductManagerAdapter;
import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.Response.OneProductResponse;
import com.example.doancuoiky.CallAPI.Response.ProductPageResponse;
import com.example.doancuoiky.CallAPI.Response.genera.Product;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Model.ProductManager;
import com.example.doancuoiky.Preferences.PreManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityManagerProduct extends AppCompatActivity {
    private ActivityResultLauncher<Intent> addProductLauncher;
    private PreManager preManager;
    private int currentPage = 0;
    private final int pageSize = 8;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

    TextView btn_back;
    Button btnAdd;
    RecyclerView recyclerView;
    ProductManagerAdapter adapter;
    List<ProductManager> productManagers ;
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);
        AnhXa();
        setContent();
        addProductLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Reset lại list và load lại
                        currentPage = 0;
                        isLastPage = false;
                        productManagers.clear();
                        adapter.notifyDataSetChanged();
                        loadProducts(currentPage);
                    }
                }
        );
        setClick();
    }
    private void AnhXa(){
        preManager = new PreManager(this);
        btn_back = findViewById(R.id.btn_back);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
    }
    private void setContent(){
        productManagers = new ArrayList<>();
        ProductManagerAdapter.OnToggleStatusListener listener = new ProductManagerAdapter.OnToggleStatusListener() {
            @Override
            public void onToggleStatus(ProductManager producManager) {
                clickChangeStatus(producManager);
            }

            @Override
            public void clickImage(ProductManager productManager) {
                Intent intent = new Intent(getApplicationContext(), Activity_Product_Add_Category.class);
                intent.putExtra("id", productManager.getId());
                startActivity(intent);
            }
        };
        adapter = new ProductManagerAdapter(productManagers,listener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        loadProducts(currentPage);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        loadProducts(++currentPage);
                    }
                }
            }
        });

    }
    private void setClick(){
        btn_back.setOnClickListener(v->{
            finish();
        });
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityManagerProduct.this, Activity_Add_Product.class);
            addProductLauncher.launch(intent);
        });
    }
    private void loadProducts(int page) {
        isLoading = true;

        apiService.getPageProduct(page, pageSize).enqueue(new Callback<ProductPageResponse>() {
            @Override
            public void onResponse(Call<ProductPageResponse> call, Response<ProductPageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> content = response.body().getBody().getContent();
                    for (Product p : content) {
                        productManagers.add(new ProductManager(
                                p.getId().intValue(),
                                p.getProductName(),
                                p.getCreateDate().substring(0, 10),
                                formatter.format(p.getPrice()),
                                p.getStockCount(),
                                p.getStatus() == 1,
                                p.getImageUrl()
                        ));
                    }
                    adapter.notifyDataSetChanged();

                    isLastPage = response.body().getBody().isLast();
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<ProductPageResponse> call, Throwable t) {
                isLoading = false;
            }
        });
    }
    void clickChangeStatus(ProductManager producManager){
        int position = productManagers.indexOf(producManager);
        apiService.changeStatusProduct(producManager.getId()).enqueue(new Callback<OneProductResponse>() {
            @Override
            public void onResponse(Call<OneProductResponse> call, Response<OneProductResponse> response) {
                if (response.isSuccessful()&&response.body().isStatus()){
                    producManager.setVisible(!producManager.isVisible());
                    adapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onFailure(Call<OneProductResponse> call, Throwable t) {
                Toast.makeText(ActivityManagerProduct.this, "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
