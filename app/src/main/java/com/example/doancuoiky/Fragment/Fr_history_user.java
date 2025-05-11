package com.example.doancuoiky.Fragment;

import static android.view.View.inflate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancuoiky.Activity_Bill_Oder;
import com.example.doancuoiky.Activity_Cart;
import com.example.doancuoiky.Adapter.OrderHistoryAdapter;
import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.Response.AccountResponse;
import com.example.doancuoiky.CallAPI.Response.BillAllResponse;
import com.example.doancuoiky.CallAPI.Response.BillResponse;
import com.example.doancuoiky.CallAPI.Response.genera.Bill;
import com.example.doancuoiky.CallAPI.Response.genera.BillDetail;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Model.Order;
import com.example.doancuoiky.Preferences.PreManager;
import com.example.doancuoiky.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fr_history_user extends Fragment {
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
    List<Order> orders = new ArrayList<>();
    private RecyclerView recyclerView ;
    OrderHistoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hisrory_user, container, false);
        anhXa(view);
        setContent();
        EventClick();
        return view;
    }
    public void anhXa(View view){
        preManager = new PreManager(getContext());
        recyclerView = view.findViewById(R.id.bill);
    }
    void setContent(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
         adapter= new OrderHistoryAdapter(getContext(), orders, order -> {
             Intent intent = new Intent(getContext(), Activity_Bill_Oder.class);
             intent.putExtra("id",order.getOrderId());
             intent.putExtra("status",order.getStatus());
             startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        LoadAPI();
    }
    void EventClick(){

    }
    void LoadAPI(){
        int idAcc = preManager.getIDAccount();
        apiService.getAllBillByIdAccount(idAcc).enqueue(new Callback<BillAllResponse>() {
            @Override
            public void onResponse(Call<BillAllResponse> call, Response<BillAllResponse> response) {
                if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                    List<Bill> lstbill = response.body().getBody();
                    orders.clear();
                    for (Bill item : lstbill){
                        orders.add(new Order(item.getId(),item.getCreatedAt().substring(0,10),item.getTotal(),item.getStatus()));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<BillAllResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadAPI();
    }
}
