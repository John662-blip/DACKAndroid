package com.example.doancuoiky.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doancuoiky.Activity_Cart;
import com.example.doancuoiky.Activity_Product_Detail;
import com.example.doancuoiky.Activity_Search_User;
import com.example.doancuoiky.Adapter.CategoryAdapter;
import com.example.doancuoiky.Adapter.PlantAdapter;
import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.Response.AccountResponse;
import com.example.doancuoiky.CallAPI.Response.CategoryResponse;
import com.example.doancuoiky.CallAPI.Response.CountResponse;
import com.example.doancuoiky.CallAPI.Response.ProductPageResponse;
import com.example.doancuoiky.CallAPI.Response.genera.Product;
import com.example.doancuoiky.CallAPI.Response.genera.ProductBody;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Config.Config;
import com.example.doancuoiky.Model.Category;
import com.example.doancuoiky.Model.PlantModel;
import com.example.doancuoiky.Preferences.PreManager;
import com.example.doancuoiky.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fr_home_user extends Fragment {
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int selectedCategoryId = -1; // -1 = All
    private final int PAGE_SIZE = 8;
    private CategoryAdapter adapter;
    private PlantAdapter pladapter;
    private List<Category> categoryList;
    List<PlantModel> plantList ;
    CircleImageView avatar;
    TextView cartQuantity;
    TextView searchBar,Cartquantity;
    RecyclerView rc_catagory;
    RecyclerView rc_item;
    ImageView btnCart;
    private PreManager preManager;
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_user, container, false);
        anhXa(view);
        setContent();
        EventClick();
        return view;
    }

    private void anhXa(View view)
    {
        preManager = new PreManager(getContext());
        avatar = view.findViewById(R.id.avatar);
        cartQuantity = view.findViewById(R.id.Cartquantity);
        searchBar = view.findViewById(R.id.search_bar);
        rc_catagory = view.findViewById(R.id.rcy_Catagory);
        rc_item = view.findViewById(R.id.itemList);
        btnCart = view.findViewById(R.id.cart);
        Cartquantity = view.findViewById(R.id.Cartquantity);
    }

    private void setContent(){
        // Catagory
        categoryList = new ArrayList<>();
        categoryList.add(new Category(-1, "All"));
        adapter = new CategoryAdapter(categoryList, category -> {

            selectedCategoryId = category.getId();
            currentPage = 0;
            isLastPage = false;
            plantList.clear();
            pladapter.notifyDataSetChanged();
            if (selectedCategoryId == -1) {
                loadPlants();
            } else {
                loadPlantsByCategory(selectedCategoryId);
            }
        });
        rc_catagory.setAdapter(adapter);

        plantList = new ArrayList<>();
        pladapter = new PlantAdapter(getContext(), plantList);
        pladapter.setOnItemClickListener(plant -> {
            Intent intent = new Intent(getContext(), Activity_Product_Detail.class);
            intent.putExtra("id", plant.getId());
            startActivity(intent);
        });
        rc_item.setAdapter(pladapter);
        rc_item.setLayoutManager(new GridLayoutManager(getContext(), 2));

        LoadAPI();
        rc_item.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == plantList.size() - 1) {
                    if (selectedCategoryId == -1) {
                        loadPlants();
                    } else {
                        loadPlantsByCategory(selectedCategoryId);
                    }
                }
            }
        });
        LoadAPICountCart();
    }

    private void EventClick(){
        btnCart.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), Activity_Cart.class);
            startActivity(intent);
        });
        searchBar.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), Activity_Search_User.class);
            startActivity(intent);
        });
    }
    private void LoadAPI(){
        apiService.getAllCategory().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.add(new Category(-1, "All"));
                    CategoryResponse body = response.body();
                    if (body.getBody() != null && !body.getBody().isEmpty()) {
                        List<CategoryResponse.Category> list = body.getBody();
                        for (CategoryResponse.Category item : list) {
                            categoryList.add(new Category(item.getId(), item.getName()));
                        }
                        adapter.notifyDataSetChanged();

                    } else {
                        Log.e("API Error", "Category list is empty or null.");
                    }
                } else {
                    Log.e("API Error", "Response is null or unsuccessful.");
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {

            }
        });
        loadPlants();
    }

    private void loadPlants() {

        if (isLoading || isLastPage) return;
        isLoading = true;

        apiService.getPageProduct(currentPage, PAGE_SIZE).enqueue(new Callback<ProductPageResponse>() {
            @Override
            public void onResponse(Call<ProductPageResponse> call, Response<ProductPageResponse> response) {
                isLoading = false;
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body().getBody().getContent();
                    if (products != null) {
                        for (Product product : products) {
                            plantList.add(new PlantModel(
                                    product.getId().intValue(),
                                    product.getProductName(),
                                    product.getStockCount(),
                                    formatter.format(product.getPrice()),
                                    product.getImageUrl()
                            ));
                        }
                        pladapter.notifyDataSetChanged();

                        if (products.size() < PAGE_SIZE) {
                            isLastPage = true;
                        } else {
                            currentPage++;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductPageResponse> call, Throwable t) {
                isLoading = false;
                Toast.makeText(getContext(), "Lỗi khi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadPlantsByCategory(int idCategory) {

        if (isLoading || isLastPage) return;
        isLoading = true;

        apiService.getPageProductByIDCategory(idCategory,currentPage, PAGE_SIZE).enqueue(new Callback<ProductPageResponse>() {
            @Override
            public void onResponse(Call<ProductPageResponse> call, Response<ProductPageResponse> response) {
                isLoading = false;
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body().getBody().getContent();
                    if (products != null) {
                        for (Product product : products) {
                            // Ép kiểu từ String về double
                            plantList.add(new PlantModel(
                                    product.getId().intValue(),
                                    product.getProductName(),
                                    product.getStockCount(),
                                    formatter.format(product.getPrice()) ,
                                    product.getImageUrl()
                            ));
                        }
                        pladapter.notifyDataSetChanged();

                        if (products.size() < PAGE_SIZE) {
                            isLastPage = true;
                        } else {
                            currentPage++;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductPageResponse> call, Throwable t) {
                isLoading = false;
                Toast.makeText(getContext(), "Lỗi khi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void LoadAPICountCart(){
        int idAccount = preManager.getIDAccount();
        apiService.getCountItemCart(idAccount).enqueue(new Callback<CountResponse>() {
            @Override
            public void onResponse(Call<CountResponse> call, Response<CountResponse> response) {
                if (response.isSuccessful()&&response.body()!=null &&response.body().isStatus()){
                    Cartquantity.setText(String.valueOf(response.body().getBody().intValue()));
                }
                else{
                    Cartquantity.setText("0");
                }
            }

            @Override
            public void onFailure(Call<CountResponse> call, Throwable t) {
                Cartquantity.setText("0");
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        LoadAPICountCart(); // Gọi lại API khi Fragment được hiển thị lại
        LoadAPI_IMAGE();
    }
    public void LoadAPI_IMAGE(){
        int idAcc = preManager.getIDAccount();
        apiService.getAccountDetailsByID(Long.valueOf(idAcc)).enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){


                        Glide.with(getContext())
                                .load(Config.URL_API_IMAGE + response.body().getBody().getImageUrl())
                                .placeholder(R.drawable.user_avatar)
                                .error(R.drawable.user_avatar)
                                .into(avatar);
                }
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {

            }
        });
    }
}
