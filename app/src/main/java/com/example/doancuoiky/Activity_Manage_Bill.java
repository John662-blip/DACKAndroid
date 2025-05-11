package com.example.doancuoiky;

import static java.security.AccessController.getContext;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancuoiky.Adapter.OrderHistoryAdapter;
import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.Response.BillAllResponse;
import com.example.doancuoiky.CallAPI.Response.BillPageResponse;
import com.example.doancuoiky.CallAPI.Response.genera.Bill;
import com.example.doancuoiky.CallAPI.Response.genera.BillPage;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Model.Order;
import com.example.doancuoiky.Preferences.PreManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Manage_Bill extends AppCompatActivity {
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
    List<Order> orders = new ArrayList<>();
    private RecyclerView recyclerView;
    OrderHistoryAdapter adapter;
    EditText edtStartDate, edtEndDate;
    TextView btn_back;
    Button btnSearch, btnClear;

    private boolean isSearchMode = false;
    private boolean isLoading = false; // NEW
    private int currentPage = 0;
    private final int pageSize = 10;

    private String startDate = null;
    private String endDate = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bill);
        anhXa();
        setContent();
        EventClick();
    }

    public void anhXa() {
        recyclerView = findViewById(R.id.recyclerView);
        edtEndDate = findViewById(R.id.edtEndDate);
        edtStartDate = findViewById(R.id.edtStartDate);
        btn_back = findViewById(R.id.btn_back);
        btnSearch = findViewById(R.id.btnSearch);
        btnClear = findViewById(R.id.btnClear);
    }

    void setContent() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderHistoryAdapter(this, orders, order -> {
            Intent intent = new Intent(Activity_Manage_Bill.this, Activity_Bill_Control.class);
            intent.putExtra("id", order.getOrderId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && layoutManager != null &&
                        orders.size() > 0 &&
                        layoutManager.findLastVisibleItemPosition() == orders.size() - 1) {
                    loadBillData(); // Lazy load next page
                }
            }
        });

        resetAndLoad(); // load ban đầu
    }

    void EventClick() {
        btn_back.setOnClickListener(v -> finish());

        edtStartDate.setOnClickListener(v -> showDatePickerDialog(edtStartDate));
        edtEndDate.setOnClickListener(v -> showDatePickerDialog(edtEndDate));

        btnSearch.setOnClickListener(v -> {
            startDate = edtStartDate.getText().toString().trim();
            endDate = edtEndDate.getText().toString().trim();
            if (!startDate.isEmpty() && !endDate.isEmpty()) {
                isSearchMode = true;
                resetAndLoad();
            }
        });

        btnClear.setOnClickListener(v -> {
            edtStartDate.setText("");
            edtEndDate.setText("");
            startDate = null;
            endDate = null;
            isSearchMode = false;
            resetAndLoad();
        });
    }

    private void resetAndLoad() {
        currentPage = 0;
        orders.clear();
        adapter.notifyDataSetChanged();
        loadBillData();
    }

    private void loadBillData() {
        isLoading = true;

        Call<BillPageResponse> call;
        if (isSearchMode) {
            String[] partsStart = startDate.split("/");
            String[] partsEnd = endDate.split("/");
            String formattedStart = partsStart[2] + "-" + partsStart[1] + "-" + partsStart[0];
            String formattedEnd = partsEnd[2] + "-" + partsEnd[1] + "-" + partsEnd[0];
            call = apiService.getPageBillDate(formattedStart, formattedEnd, currentPage, pageSize);
        } else {
            call = apiService.getAllPageBill(currentPage, pageSize);
        }

        call.enqueue(new Callback<BillPageResponse>() {
            @Override
            public void onResponse(Call<BillPageResponse> call, Response<BillPageResponse> response) {
                isLoading = false;
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    List<Bill> bills = response.body().getBody().getContent();
                    for (Bill bill : bills) {
                        orders.add(new Order(
                                bill.getId(),
                                bill.getCreatedAt().substring(0, 10),
                                bill.getTotal(),
                                bill.getStatus()
                        ));
                    }
                    adapter.notifyDataSetChanged();
                    currentPage++;
                }
            }

            @Override
            public void onFailure(Call<BillPageResponse> call, Throwable t) {
                isLoading = false;
                // TODO: xử lý lỗi (hiện Toast, Log, hoặc retry)
            }
        });
    }

    private void showDatePickerDialog(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    editText.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        edtStartDate.setText("");
        edtEndDate.setText("");
        startDate = null;
        endDate = null;
        isSearchMode = false;
        resetAndLoad();
    }
}