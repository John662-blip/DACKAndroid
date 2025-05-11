package com.example.doancuoiky;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.Response.AccountResponse;
import com.example.doancuoiky.CallAPI.Response.CartResponse;
import com.example.doancuoiky.CallAPI.Response.OneProductHasCategory;
import com.example.doancuoiky.CallAPI.Response.genera.CartItem;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Config.Config;
import com.example.doancuoiky.Model.Product;
import com.example.doancuoiky.Preferences.PreManager;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Product_Detail extends AppCompatActivity {

    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
    TextView btnBack;
    ImageView imageView;
    TextView tvName;
    TextView tvDescription,price;
    TextView tvHumadity;
    TextView tvTemplate;
    Button btnAddCart;
    int idProduct;
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
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
        btnAddCart = findViewById(R.id.btn_add_cart);
        price = findViewById(R.id.price);
    }
    void loadContent(){
        Bundle extras = getIntent().getExtras();
        if (extras!= null) {
            idProduct = extras.getInt("id",-1);
            if (idProduct!=-1){
                loadApi();
            }
        }
    }
    void setClick(){
        btnBack.setOnClickListener(v -> finish());
        btnAddCart.setOnClickListener(v->{
            CallAPIAdd(idProduct);
        });
    }
    void loadApi(){
        apiService.getOneProduct(idProduct).enqueue(new Callback<OneProductHasCategory>() {
            @Override
            public void onResponse(Call<OneProductHasCategory> call, Response<OneProductHasCategory> response) {
                if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                    OneProductHasCategory.Product product = response.body().getBody();
                    Glide.with(Activity_Product_Detail.this)
                            .load(Config.URL_API_IMAGE+product.getImageUrl())
                            .placeholder(R.drawable.triangle_exclamation_solid) // ảnh tạm khi đang load
                            .error(R.drawable.triangle_exclamation_solid)       // ảnh lỗi nếu không load được
                            .into(imageView);
                    tvName.setText(product.getProductName());
                    tvDescription.setText(product.getDescription());
                    tvTemplate.setText(String.valueOf(product.getTemperature()));
                    tvHumadity.setText(String.valueOf(product.getHumadity()));
                    price.setText(formatter.format(product.getPrice()));
                }
            }

            @Override
            public void onFailure(Call<OneProductHasCategory> call, Throwable t) {
                Toast.makeText(Activity_Product_Detail.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    void CallAPIAdd(int idProduct){
        int idAcc = preManager.getIDAccount();
        apiService.addItemInCart(idAcc,idProduct).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                    Toast.makeText(Activity_Product_Detail.this, "Added product successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Activity_Product_Detail.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(Activity_Product_Detail.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
