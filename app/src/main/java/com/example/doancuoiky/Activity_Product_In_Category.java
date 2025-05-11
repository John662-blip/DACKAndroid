package com.example.doancuoiky;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancuoiky.Adapter.MyProductAdapter;
import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.Response.OneProductResponse;
import com.example.doancuoiky.CallAPI.Response.ProductResponse;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Model.MyProduct;
import com.example.doancuoiky.Preferences.PreManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Product_In_Category extends AppCompatActivity {
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
    TextView btn_back,tvCategoryName;
    RecyclerView recyclerView;
    List<MyProduct> myProducts ;
    MyProductAdapter adapter;
    int idCategory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_by_category);
        AnhXa();
        setContent();
        setClick();
    }
    private void AnhXa(){
        btn_back = findViewById(R.id.btn_back);
        tvCategoryName = findViewById(R.id.tvTitle);
        recyclerView = findViewById(R.id.recyclerView);
    }
    private void setContent(){
        myProducts = new ArrayList<>();
        MyProductAdapter.OnClickListener listener = new MyProductAdapter.OnClickListener() {
            @Override
            public void onDeleteClick(MyProduct product) {
                deleteAdapter(product);
            }

            @Override
            public void onCLick(MyProduct product) {

            }
        };
        adapter = new MyProductAdapter(this, myProducts,listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        Bundle extras = getIntent().getExtras();
        if (extras!= null) {
            idCategory = extras.getInt("id", -1);
            String nameCategory = extras.getString("name","");
            tvCategoryName.setText(nameCategory);
            if (idCategory!=-1){
                loadAPI();
            }
        }
    }
    private void setClick(){
        btn_back.setOnClickListener(v->finish());
    }
    void loadAPI(){
        apiService.getProductCategory(idCategory).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                    List<ProductResponse.Product> list = response.body().getBody();
                    for (ProductResponse.Product item : list){
                        myProducts.add(new MyProduct(item.getId(),item.getImageUrl(),
                                item.getProductName(),item.getPrice(),item.getCreateDate().substring(0,10)
                        ,item.getStockCount()));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {

            }
        });
    }
    void deleteAdapter(MyProduct myProduct){
        int position = myProducts.indexOf(myProduct);
        apiService.ProductAddCategory(Long.valueOf(myProduct.getIdProduct()),null).enqueue(new Callback<OneProductResponse>() {
            @Override
            public void onResponse(Call<OneProductResponse> call, Response<OneProductResponse> response) {
                if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                    myProducts.remove(myProduct);
                    adapter.notifyItemRemoved(position);
                }
                else {
                    Toast.makeText(Activity_Product_In_Category.this, "Delete Fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OneProductResponse> call, Throwable t) {
                Toast.makeText(Activity_Product_In_Category.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
