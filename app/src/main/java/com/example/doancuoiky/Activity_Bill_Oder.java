package com.example.doancuoiky;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancuoiky.Adapter.ProductBillAdapter;
import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.Response.BillResponse;
import com.example.doancuoiky.CallAPI.Response.genera.BillDetail;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Model.ProductBill;
import com.example.doancuoiky.Preferences.PreManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Bill_Oder extends AppCompatActivity {
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
    TextView tvBack ;
    RecyclerView rcView;
    private TextView tvSalary;
    private TextView shipPrice;
    private TextView tvName;
    private TextView tvTotal;
    private Button btn_Cancel;
    private ProductBillAdapter adapter;
    private List<ProductBill> productList;
    private int idBill;
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        AnhXa();
        LoadContent();
        setClick();
    }
    private void AnhXa(){
        preManager = new PreManager(this);
        tvBack = findViewById(R.id.btn_back);
        rcView = findViewById(R.id.recyclerView);
        tvSalary = findViewById(R.id.salary);
        shipPrice = findViewById(R.id.shipprice);
        tvTotal = findViewById(R.id.total);
        btn_Cancel = findViewById(R.id.btn_cancel);
        tvName = findViewById(R.id.textView7);
    }
    private void LoadContent(){
        productList = new ArrayList<>();
        rcView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        rcView.setLayoutManager(new LinearLayoutManager(this));
        ProductBillAdapter.OnItemClickListener listener = new ProductBillAdapter.OnItemClickListener() {
            @Override
            public void onImageClick(ProductBill product) {
                Intent intent = new Intent(Activity_Bill_Oder.this, Activity_Product_Detail.class);
                intent.putExtra("id", product.getId().intValue());
                startActivity(intent);
            }
        };
        adapter = new ProductBillAdapter(this,productList,listener);
        rcView.setAdapter(adapter);
        Bundle extras = getIntent().getExtras();
        if (extras!= null) {
            idBill = extras.getInt("id",-1);
            tvName.setText("Order Code: #"+idBill);
            int status = extras.getInt("status");
            if (status != 0){
                btn_Cancel.setVisibility(View.INVISIBLE);
            }
            LoadAPI();
        }
    }
    private void setClick(){
        tvBack.setOnClickListener(v->finish());
        btn_Cancel.setOnClickListener(v->{
            CallAPICancel();
        });
    }
    void LoadAPI(){
        apiService.getBilllByBillID(idBill).enqueue(new Callback<BillResponse>() {
            @Override
            public void onResponse(Call<BillResponse> call, Response<BillResponse> response) {
                if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                    productList.clear();
                    List<BillDetail> lstBillDetail = response.body().getBody().getBillDetails();
                    Double total = 0D;
                    for (BillDetail item : lstBillDetail){
                        productList.add(new ProductBill(item.getProduct().getId(),item.getProduct().getProductName(),item.getQuantity(),item.getProduct().getPrice(),item.getProduct().getImageUrl()));
                        total+=item.getQuantity()*item.getProduct().getPrice();
                    }
                    adapter.notifyDataSetChanged();

                    tvSalary.setText(formatter.format(total.intValue()));
                    Double ship = 70000D;
                    shipPrice.setText(formatter.format(ship.intValue()));
                    Double price = ship+total;
                    tvTotal.setText(formatter.format(price.intValue()));
                }
            }

            @Override
            public void onFailure(Call<BillResponse> call, Throwable t) {

            }
        });
    }
    void CallAPICancel(){
        apiService.cancelBill(idBill).enqueue(new Callback<BillResponse>() {
            @Override
            public void onResponse(Call<BillResponse> call, Response<BillResponse> response) {
                if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                    if (response.body().getBody().getStatus() != 0){
                        btn_Cancel.setVisibility(View.INVISIBLE);
                        Toast.makeText(Activity_Bill_Oder.this, "Cancel order successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BillResponse> call, Throwable t) {
                Toast.makeText(Activity_Bill_Oder.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
