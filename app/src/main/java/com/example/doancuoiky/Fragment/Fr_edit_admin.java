package com.example.doancuoiky.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancuoiky.ActivityManagerProduct;
import com.example.doancuoiky.Activity_Login;
import com.example.doancuoiky.Activity_Manage_Bill;
import com.example.doancuoiky.Activity_Manage_Category;
import com.example.doancuoiky.Activity_Register;
import com.example.doancuoiky.Adapter.OrderHistoryAdapter;
import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Model.Order;
import com.example.doancuoiky.Preferences.PreManager;
import com.example.doancuoiky.R;

import java.util.ArrayList;
import java.util.List;

public class Fr_edit_admin extends Fragment {
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
    Button btn_manage_category,btn_manage_products,btn_manage_bill;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_admin, container, false);
        anhXa(view);
        EventClick();
        return view;
    }
    private void anhXa(View view){
        preManager = new PreManager(getContext());
        btn_manage_category = view.findViewById(R.id.btn_manage_category);
        btn_manage_products = view.findViewById(R.id.btn_manage_products);
        btn_manage_bill = view.findViewById(R.id.btn_manage_bill);
    }
    private void EventClick(){
        btn_manage_category.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), Activity_Manage_Category.class);
            startActivity(intent);
        });
        btn_manage_products.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), ActivityManagerProduct.class);
            startActivity(intent);
        });
        btn_manage_bill.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), Activity_Manage_Bill.class);
            startActivity(intent);
        });
    }
}
