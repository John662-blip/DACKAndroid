package com.example.doancuoiky;

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
import com.example.doancuoiky.CallAPI.Response.genera.Account;
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

public class Activity_Bill_Control extends AppCompatActivity {
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
    TextView btn_back,tvStatus,tvUserId,tvUserName,tvUserAddress,tvUserPhone,tvTotal2;
    RecyclerView rvProductList;
    Button btnCancelOrder,btnChangeStatus;
    private ProductBillAdapter adapter;
    private List<ProductBill> productList;
    int idBill;
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_control);
        AnhXa();
        setContent();
        setClick();
    }
    private void AnhXa(){
        preManager = new PreManager(this);
        btn_back = findViewById(R.id.btn_back);
        tvStatus = findViewById(R.id.tvStatus);
        tvUserId = findViewById(R.id.tvUserId);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserAddress = findViewById(R.id.tvUserAddress);
        tvUserPhone = findViewById(R.id.tvUserPhone);
        tvTotal2 = findViewById(R.id.tvTotal2);
        rvProductList = findViewById(R.id.rvProductList);
        btnCancelOrder = findViewById(R.id.btnCancelOrder);
        btnChangeStatus = findViewById(R.id.btnChangeStatus);
    }
    private void setContent(){
        Bundle extras = getIntent().getExtras();
        productList = new ArrayList<>();
        rvProductList.setLayoutManager(new LinearLayoutManager(this));
        productList.add(new ProductBill(1L, "Aloe Vera", 2, 150000, null));
        ProductBillAdapter.OnItemClickListener listener = new ProductBillAdapter.OnItemClickListener() {
            @Override
            public void onImageClick(ProductBill product) {

            }
        };
        adapter = new ProductBillAdapter(this, productList, listener);
        rvProductList.setAdapter(adapter);
        if (extras!= null) {
            idBill = extras.getInt("id",-1);
            CallAPIAccount(idBill);
        }
    }
    private void setClick(){
        btn_back.setOnClickListener(v->{finish();});
        btnChangeStatus.setOnClickListener(v->{
            ChangeStatusBill(idBill);
        });
        btnCancelOrder.setOnClickListener(v->{
            CacelBill(idBill);
        });
    }
    private void CallAPIAccount(int idBill){
        apiService.getBilllByBillID(idBill).enqueue(new Callback<BillResponse>() {
            @Override
            public void onResponse(Call<BillResponse> call, Response<BillResponse> response) {
                if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                    Account account = response.body().getBody().getAccount();
                    tvUserId.setText("User ID: "+account.getId());
                    tvUserName.setText("Name: "+account.getName());
                    tvUserAddress.setText("Address: "+response.body().getBody().getAddress());
                    tvUserPhone.setText("Phone: "+account.getPhone());
                    tvStatus.setText(getStatusText(response.body().getBody().getStatus()));
                    List<BillDetail> lstBillDt = response.body().getBody().getBillDetails();
                    productList.clear();
                    Double total = 0D;
                    if (response.body().getBody().getStatus()==3){
                        btnCancelOrder.setVisibility(View.INVISIBLE);
                        btnChangeStatus.setVisibility(View.INVISIBLE);
                    }
                    if (response.body().getBody().getStatus()==2){
                        btnChangeStatus.setVisibility(View.INVISIBLE);
                    }
                    for (BillDetail item : lstBillDt){
                        productList.add(new ProductBill(item.getProduct().getId(), item.getProduct().getProductName(),item.getQuantity(),item.getProduct().getPrice(),item.getProduct().getImageUrl()));
                        total += item.getQuantity()*item.getProduct().getPrice();
                    }
                    adapter.notifyDataSetChanged();
                    total += 70000;
                    tvTotal2.setText("Total : "+formatter.format(total));
                }
            }

            @Override
            public void onFailure(Call<BillResponse> call, Throwable t) {

            }
        });
    }
    void ChangeStatusBill(int idBill){
        apiService.changeStatusBill(idBill).enqueue(new Callback<BillResponse>() {
            @Override
            public void onResponse(Call<BillResponse> call, Response<BillResponse> response) {
                if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                    Toast.makeText(Activity_Bill_Control.this, "Successful change", Toast.LENGTH_SHORT).show();
                    tvStatus.setText(getStatusText(response.body().getBody().getStatus()));
                    if (response.body().getBody().getStatus()==2){
                        btnChangeStatus.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    Toast.makeText(Activity_Bill_Control.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BillResponse> call, Throwable t) {
                Toast.makeText(Activity_Bill_Control.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    void CacelBill(int idBill){
        apiService.cancelBill(idBill).enqueue(new Callback<BillResponse>() {
            @Override
            public void onResponse(Call<BillResponse> call, Response<BillResponse> response) {
                if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                    Toast.makeText(Activity_Bill_Control.this, "Successful change", Toast.LENGTH_SHORT).show();
                    tvStatus.setText(getStatusText(response.body().getBody().getStatus()));
                    if (response.body().getBody().getStatus()==3){
                        btnCancelOrder.setVisibility(View.INVISIBLE);
                        btnChangeStatus.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    Toast.makeText(Activity_Bill_Control.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BillResponse> call, Throwable t) {
                Toast.makeText(Activity_Bill_Control.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getStatusText(int status) {
        switch (status) {
            case 0: return "Chờ xác nhận";
            case 1: return "Đang giao hàng";
            case 2: return "Đã hoàn thành";
            case 3: return "Hủy đơn";
            default: return "Không rõ";
        }
    }
}
