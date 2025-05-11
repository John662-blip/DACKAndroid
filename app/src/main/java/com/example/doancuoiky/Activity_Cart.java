package com.example.doancuoiky;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancuoiky.Adapter.ProductAdapter;
import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.Response.BillResponse;
import com.example.doancuoiky.CallAPI.Response.CartResponse;
import com.example.doancuoiky.CallAPI.Response.genera.CartItem;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Model.Product;
import com.example.doancuoiky.Preferences.PreManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Cart extends AppCompatActivity {
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

    private TextView tvBack;
    private TextView tvSalary;
    private TextView shipPrice;
    private TextView tvTotal;
    private Button btn_Cart;
    private RecyclerView recyclerView;
    private List<Product> productList ;
    private ProductAdapter adapter;
    private Double price = 0D;
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycart);
        AnhXa();
        LoadData();
        SetClick();
        LoadAPI();
    }
    void AnhXa(){
        preManager = new PreManager(this);
        tvBack = findViewById(R.id.btn_back);
        tvSalary = findViewById(R.id.salary);
        shipPrice = findViewById(R.id.shipprice);
        tvTotal = findViewById(R.id.total);
        btn_Cart = findViewById(R.id.btn_buy);
        recyclerView = findViewById(R.id.recyclerView);
    }
    void LoadData(){
        productList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ProductAdapter.OnProductActionListener listener = new ProductAdapter.OnProductActionListener() {
            @Override
            public void onClickImage(Product product) {
                Intent intent = new Intent(getApplicationContext(), Activity_Product_Detail.class);
                intent.putExtra("id", product.getIdProduct());
                startActivity(intent);
            }

            @Override
            public void onIncrease(Product product) {
                CallAPIAdd(product.getIdProduct());
            }

            @Override
            public void onDecrease(Product product) {
                CallAPIRemove(product.getIdProduct());
            }

            @Override
            public void onDelete(Product product) {
                CallAPIDelete(product.getIdProduct());
            }
        };
        adapter = new ProductAdapter(this, productList, listener);
        recyclerView.setAdapter(adapter);
    }

    void SetClick(){
        tvBack.setOnClickListener(v -> finish());
        btn_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productList.size()==0){
                    Toast.makeText(Activity_Cart.this, "There are no products to order.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Tạo dialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Cart.this);
                builder.setTitle("Enter your delivery address");

                // Tạo EditText để nhập địa chỉ
                final EditText input = new EditText(Activity_Cart.this);
                input.setHint("e.g. 123 Green Street");
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
                builder.setView(input);

                // Nút xác nhận
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String address = input.getText().toString().trim();
                        if (address.isEmpty()) {
                            Toast.makeText(Activity_Cart.this, "Please enter an address", Toast.LENGTH_SHORT).show();
                        } else {
                            CallAPIOrder(address);
                        }
                    }
                });

                // Nút hủy
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Hiển thị dialog
                builder.show();
            }
        });
    }
    void  CallAPIOrder(String address){
        int idAcc = preManager.getIDAccount();
        apiService.order(idAcc,address).enqueue(new Callback<BillResponse>() {
            @Override
            public void onResponse(Call<BillResponse> call, Response<BillResponse> response) {
                if (response.isSuccessful()){
                    if (response.body()!=null && response.body().isStatus()){
                        Toast.makeText(Activity_Cart.this, "Order successful", Toast.LENGTH_SHORT).show();
                        Double total = 0D;
                        productList.clear();
                        adapter.notifyDataSetChanged();
                        tvSalary.setText(formatter.format(total.intValue()));
                        Double ship = 70000D;
                        shipPrice.setText(formatter.format(ship.intValue()));
                        price = ship+total;
                        tvTotal.setText(formatter.format(price.intValue()));
                    }
                    else{
                        Toast.makeText(Activity_Cart.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BillResponse> call, Throwable t) {

            }
        });
    }
    void LoadAPI(){
        int idAcc = preManager.getIDAccount();
        apiService.getItemCartByIDAccount(idAcc).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                    List<CartItem> lst = response.body().getBody().getCartItems();
                    Double total = 0D;
                    for (CartItem item : lst){
                        total+=item.getQuantity()*item.getProduct().getPrice();
                        productList.add(new Product(item.getProduct().getId().intValue(), item.getProduct().getProductName(), item.getQuantity(), item.getProduct().getImageUrl(),Double.valueOf(item.getProduct().getPrice())));
                    }
                    adapter.notifyDataSetChanged();
                    tvSalary.setText(formatter.format(total.intValue()));
                    Double ship = 70000D;
                    shipPrice.setText(formatter.format(ship.intValue()));
                    price = ship+total;
                    tvTotal.setText(formatter.format(price.intValue()));
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {

            }
        });
    }
    void CallAPIAdd(int idProduct){
        int idAcc = preManager.getIDAccount();
        apiService.addItemInCart(idAcc,idProduct).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                    List<CartItem> lst = response.body().getBody().getCartItems();
                    Double total = 0D;
                    productList.clear();
                    for (CartItem item : lst){
                        total+=item.getQuantity()*item.getProduct().getPrice();
                        productList.add(new Product(item.getProduct().getId().intValue(), item.getProduct().getProductName(), item.getQuantity(), item.getProduct().getImageUrl(),Double.valueOf(item.getProduct().getPrice())));
                    }
                    adapter.notifyDataSetChanged();
                    tvSalary.setText(formatter.format(total.intValue()));
                    Double ship = 70000D;
                    shipPrice.setText(formatter.format(ship.intValue()));
                    price = ship+total;
                    tvTotal.setText(formatter.format(price.intValue()));
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {

            }
        });
    }
    void CallAPIRemove(int idProduct){
        int idAcc = preManager.getIDAccount();
        apiService.removeItemInCart(idAcc,idProduct).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                    List<CartItem> lst = response.body().getBody().getCartItems();
                    Double total = 0D;
                    productList.clear();
                    for (CartItem item : lst){
                        total+=item.getQuantity()*item.getProduct().getPrice();
                        productList.add(new Product(item.getProduct().getId().intValue(), item.getProduct().getProductName(), item.getQuantity(), item.getProduct().getImageUrl(),Double.valueOf(item.getProduct().getPrice())));
                    }
                    adapter.notifyDataSetChanged();
                    tvSalary.setText(formatter.format(total.intValue()));
                    Double ship = 70000D;
                    shipPrice.setText(formatter.format(ship.intValue()));
                    price = ship+total;
                    tvTotal.setText(formatter.format(price.intValue()));
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {

            }
        });
    }
    void CallAPIDelete(int idProduct){
        int idAcc = preManager.getIDAccount();
        apiService.deleteItemInCart(idAcc,idProduct).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                    List<CartItem> lst = response.body().getBody().getCartItems();
                    Double total = 0D;
                    productList.clear();
                    for (CartItem item : lst){
                        total+=item.getProduct().getPrice();
                        productList.add(new Product(item.getProduct().getId().intValue(), item.getProduct().getProductName(), item.getQuantity(), item.getProduct().getImageUrl(),Double.valueOf(item.getProduct().getPrice())));
                    }
                    adapter.notifyDataSetChanged();
                    tvSalary.setText(formatter.format(total.intValue()));
                    Double ship = 70000D;
                    shipPrice.setText(formatter.format(ship.intValue()));
                    price = ship+total;
                    tvTotal.setText(formatter.format(price.intValue()));
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {

            }
        });
    }
}
