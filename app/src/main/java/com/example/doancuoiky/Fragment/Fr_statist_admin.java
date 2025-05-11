package com.example.doancuoiky.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.Response.ObjectResponse;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Preferences.PreManager;
import com.example.doancuoiky.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fr_statist_admin extends Fragment {
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

    private Spinner spinnerMonth, spinnerYear;
    private BarChart barChartRevenue, barChartTopProducts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statist_admin, container, false);
        anhXa(view);
        setupSpinners();
        return view;
    }

    private void anhXa(View view){
        preManager = new PreManager(getContext());
        spinnerMonth = view.findViewById(R.id.spinner_month);
        spinnerYear = view.findViewById(R.id.spinner_year);
        barChartRevenue = view.findViewById(R.id.barChartRevenue);
        barChartTopProducts = view.findViewById(R.id.barChartTopProducts);
    }

    private void setupSpinners() {
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        // Setup month spinner
        List<Integer> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) months.add(i);
        ArrayAdapter<Integer> monthAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, months);
        spinnerMonth.setAdapter(monthAdapter);
        spinnerMonth.setSelection(currentMonth - 1);

        // Setup year spinner
        List<Integer> years = new ArrayList<>();
        for (int i = currentYear - 4; i <= currentYear + 4; i++) years.add(i);
        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, years);
        spinnerYear.setAdapter(yearAdapter);
        spinnerYear.setSelection(4); // currentYear - (currentYear - 4) = 4

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int year = (int) parent.getItemAtPosition(position);
                loadRevenueByYear(year);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int month = (int) parent.getItemAtPosition(position);
                loadTopProductsByMonth(month);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Gọi ngay khi vào màn
        loadRevenueByYear(currentYear);
        loadTopProductsByMonth(currentMonth);
    }

    private void loadRevenueByYear(int year) {
        apiService.getMonthlyRevenueByYear(year).enqueue(new Callback<ObjectResponse>() {
            @Override
            public void onResponse(Call<ObjectResponse> call, Response<ObjectResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    List<List<Object>> data = response.body().getBody();

                    // Tạo 1 mảng 12 phần tử, mặc định 0
                    float[] monthlyRevenue = new float[12];
                    for (List<Object> row : data) {
                        int month = ((Number) row.get(0)).intValue();
                        float revenue = ((Number) row.get(1)).floatValue();
                        monthlyRevenue[month - 1] = revenue;  // tháng 1 -> index 0
                    }

                    List<BarEntry> entries = new ArrayList<>();
                    List<String> labels = new ArrayList<>();
                    for (int i = 0; i < 12; i++) {
                        entries.add(new BarEntry(i, monthlyRevenue[i]));
                        labels.add("T" + (i + 1));
                    }

                    BarDataSet dataSet = new BarDataSet(entries, "Doanh thu theo tháng");
                    dataSet.setColor(Color.BLUE);

                    BarData barData = new BarData(dataSet);
                    barData.setBarWidth(0.9f);
                    barData.setValueTextSize(10f); // hiện giá trị trên cột

                    barChartRevenue.setData(barData);
                    barChartRevenue.setFitBars(true);

                    // Thiết lập trục X
                    XAxis xAxis = barChartRevenue.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularity(1f);
                    xAxis.setLabelRotationAngle(0f);

                    // Thiết lập trục Y
                    barChartRevenue.getAxisLeft().setAxisMinimum(0f); // luôn từ 0
                    barChartRevenue.getAxisLeft().setGranularity(1f);
                    barChartRevenue.getAxisRight().setEnabled(false); // tắt trục phải

                    // Format giá trị trục Y (dấu phẩy ngăn cách)
                    barChartRevenue.getAxisLeft().setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return String.format("%,.0f", value);
                        }
                    });

                    barChartRevenue.invalidate();
                }
            }

            @Override public void onFailure(Call<ObjectResponse> call, Throwable t) {
                Log.e("API", "Failed to load revenue", t);
            }
        });
    }
    private void loadTopProductsByMonth(int month) {
        apiService.findTopSellingProductsByMonth(month).enqueue(new Callback<ObjectResponse>() {
            @Override
            public void onResponse(Call<ObjectResponse> call, Response<ObjectResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    List<List<Object>> data = response.body().getBody();
                    List<BarEntry> entries = new ArrayList<>();
                    List<String> productNames = new ArrayList<>();

                    for (int i = 0; i < data.size(); i++) {
                        List<Object> row = data.get(i);
                        int id = ((Number) row.get(0)).intValue();
                        String name = (String) row.get(1);
                        int quantity = ((Number) row.get(2)).intValue();

                        // Giới hạn độ dài tên sản phẩm
                        if (name.length() > 10) {
                            name = name.substring(0, 10) + "...";  // Cắt và thêm dấu "..."
                        }

                        entries.add(new BarEntry(i, quantity));
                        productNames.add(name);
                    }

                    BarDataSet dataSet = new BarDataSet(entries, "Top sản phẩm bán chạy");
                    dataSet.setColor(Color.MAGENTA);
                    BarData barData = new BarData(dataSet);
                    barChartTopProducts.setData(barData);

                    // Cài đặt tên sản phẩm cho các label trục X
                    XAxis xAxis = barChartTopProducts.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(productNames));
                    xAxis.setGranularity(1f);
                    xAxis.setLabelRotationAngle(-45);  // Đổi góc để dễ đọc hơn

                    // Thay đổi kích thước text label trên trục X
                    xAxis.setTextSize(10f);  // Chỉnh lại kích thước chữ cho label trục X

                    barChartTopProducts.invalidate();
                }
            }

            @Override
            public void onFailure(Call<ObjectResponse> call, Throwable t) {
                Log.e("API", "Failed to load top products", t);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        int selectedYear = (int) spinnerYear.getSelectedItem();
        int selectedMonth = (int) spinnerMonth.getSelectedItem();
        loadRevenueByYear(selectedYear);
        loadTopProductsByMonth(selectedMonth);
    }
}
