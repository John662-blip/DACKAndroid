package com.example.doancuoiky;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.Response.CategoryResponse;
import com.example.doancuoiky.CallAPI.Response.OneProductHasCategory;
import com.example.doancuoiky.CallAPI.Response.OneProductResponse;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Config.Config;
import com.example.doancuoiky.Preferences.PreManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Product_Add_Category extends AppCompatActivity {
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
    TextView btnBack;
    ImageView imageView;
    TextView tvName,price;
    TextView tvDescription;
    TextView tvHumadity;
    TextView tvTemplate;
    Spinner spinner_category;
    Button btn_save;
    int idProduct;
    Long idCategory;
    List<CategoryPR> listCategory = new ArrayList<>();
    ArrayAdapter<CategoryPR> adapter;
    private class CategoryPR{
        int id;
        String name;

        public CategoryPR(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add_category);
        listCategory.add(new CategoryPR(-1,"None"));
        AnhXa();
        loadContent();
        setClick();
    }
    void AnhXa(){
        preManager = new PreManager(this);
        btnBack = findViewById(R.id.btn_back);
        imageView = findViewById(R.id.iv_product_image);
        tvName = findViewById(R.id.name);
        tvDescription = findViewById(R.id.tv_description);
        tvTemplate = findViewById(R.id.template);
        tvHumadity = findViewById(R.id.humadity);
        spinner_category = findViewById(R.id.spinner_category);
        btn_save = findViewById(R.id.btn_save);
        price = findViewById(R.id.price);
    }
    void loadContent(){
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listCategory
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if (extras!= null) {
            idProduct = extras.getInt("id",-1);
            if (idProduct!=-1){
                apiService.getOneProduct(idProduct).enqueue(new Callback<OneProductHasCategory>() {
                    @Override
                    public void onResponse(Call<OneProductHasCategory> call, Response<OneProductHasCategory> response) {
                        if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                            OneProductHasCategory resp = response.body();
                            Glide.with(Activity_Product_Add_Category.this)
                                    .load(Config.URL_API_IMAGE+resp.getBody().getImageUrl())
                                    .placeholder(R.drawable.triangle_exclamation_solid)
                                    .error(R.drawable.triangle_exclamation_solid)
                                    .into(imageView);
                            tvName.setText(resp.getBody().getProductName());
                            tvDescription.setText(resp.getBody().getDescription());
                            tvTemplate.setText(String.valueOf(resp.getBody().getTemperature()));
                            tvHumadity.setText(String.valueOf(resp.getBody().getHumadity()));
                            idCategory = resp.getBody().getCategoryId();
                            loadCategory(idCategory,idProduct);
                            price.setText(resp.getBody().getPrice()+" đ");
                        }
                    }

                    @Override
                    public void onFailure(Call<OneProductHasCategory> call, Throwable t) {
                        Toast.makeText(Activity_Product_Add_Category.this, "Server error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
    void setClick(){
        btnBack.setOnClickListener(v -> finish());
        btn_save.setOnClickListener(v->{
            Long idCa = null;
            CategoryPR select = (CategoryPR) spinner_category.getSelectedItem();
            if (select.getId()==-1) idCa = null ;
            else idCa = Long.valueOf(select.getId());
            apiService.ProductAddCategory(Long.valueOf(idProduct),idCa).enqueue(new Callback<OneProductResponse>() {
                @Override
                public void onResponse(Call<OneProductResponse> call, Response<OneProductResponse> response) {
                    if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                        Toast.makeText(Activity_Product_Add_Category.this, "Update successful", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Activity_Product_Add_Category.this, "Update failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<OneProductResponse> call, Throwable t) {
                    Toast.makeText(Activity_Product_Add_Category.this, "Server not responding", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    void loadCategory(Long idCategory,int idProduct){

        apiService.getAllCategory().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                    listCategory.clear();
                    listCategory.add(new CategoryPR(-1, "None"));
                    for (CategoryResponse.Category category : response.body().getBody()) {
                        listCategory.add(new CategoryPR(category.getId(), category.getName()));
                    }
                    if (idCategory!=null) {
                        for (int i = 0; i < listCategory.size(); i++) {
                            if (listCategory.get(i).getId() == idCategory) {
                                spinner_category.setSelection(i);
                                break;
                            }
                        }
                    }else{
                        spinner_category.setSelection(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {

            }
        });
    }
}
