package com.example.doancuoiky;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancuoiky.Adapter.ProductCategoryAdapter;
import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.Response.CategoryResponse;
import com.example.doancuoiky.CallAPI.Response.OneCategoryResponse;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Model.Category;
import com.example.doancuoiky.Preferences.PreManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Manage_Category extends AppCompatActivity {
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
    TextView btn_back;
    EditText edtCategoryName;
    Button btnAdd;
    RecyclerView recyclerView;
    ProductCategoryAdapter adapter;
    List<Category> lstCategory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);
        AnhXa();
        setContent();
        setClick();
    }
    private void AnhXa(){
        preManager = new PreManager(this);
        btn_back = findViewById(R.id.btn_back);
        edtCategoryName = findViewById(R.id.edtCategoryName);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
    }
    private void setContent(){
        lstCategory = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ProductCategoryAdapter(lstCategory, new ProductCategoryAdapter.OnProductCategoryActionListener() {
            @Override
            public void onEdit(Category category) {
                EditText editText = new EditText(Activity_Manage_Category.this);
                editText.setText(category.getName());
                editText.setSelection(editText.getText().length()); // Di chuyển con trỏ về cuối

                new androidx.appcompat.app.AlertDialog.Builder(Activity_Manage_Category.this)
                        .setTitle("Edit category")
                        .setMessage("Enter new category name:")
                        .setView(editText)
                        .setPositiveButton("Update", (dialog, which) -> {
                            String newName = editText.getText().toString().trim();
                            if (!newName.isEmpty()) {
                                apiService.updateCategory(category.getId(), newName).enqueue(new Callback<OneCategoryResponse>() {
                                    @Override
                                    public void onResponse(Call<OneCategoryResponse> call, Response<OneCategoryResponse> response) {
                                        if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                                            category.setName(newName);
                                            adapter.notifyItemChanged(lstCategory.indexOf(category)); // refresh item
                                            Toast.makeText(Activity_Manage_Category.this, "Update successful", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Activity_Manage_Category.this, "Update failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<OneCategoryResponse> call, Throwable t) {
                                        Toast.makeText(Activity_Manage_Category.this, "Server connection error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(Activity_Manage_Category.this, "Name cannot be blank", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }

            @Override
            public void onDelete(Category category) {
                int position = lstCategory.indexOf(category);
                if (position != -1) {
                    apiService.deleteCategory(category.getId()).enqueue(new Callback<OneCategoryResponse>() {
                        @Override
                        public void onResponse(Call<OneCategoryResponse> call, Response<OneCategoryResponse> response) {
                            if (response.isSuccessful() && response.body().isStatus()) {
                                lstCategory.remove(position);
                                adapter.notifyItemRemoved(position);
                            } else {
                                Log.e("API Error", "Delete failed");
                            }
                        }

                        @Override
                        public void onFailure(Call<OneCategoryResponse> call, Throwable t) {
                            Toast.makeText(Activity_Manage_Category.this, "Server error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.e("DELETE", "Category not found in list");
                }
            }
            @Override
            public void onClick(Category category) {
                Intent intent = new Intent(Activity_Manage_Category.this, Activity_Product_In_Category.class);
                intent.putExtra("id",category.getId());
                intent.putExtra("name",category.getName());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        apiService.getAllCategory().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    lstCategory.clear();
                    CategoryResponse body = response.body();
                    if (body.getBody() != null && !body.getBody().isEmpty()) {
                        List<CategoryResponse.Category> list = body.getBody();
                        for (CategoryResponse.Category item : list) {
                            lstCategory.add(new Category(item.getId(), item.getName()));
                        }
                        adapter.notifyDataSetChanged();

                    } else {
                        Log.e("API Error", "Category list is empty or null.");
                    }
                } else {
                    Log.e("API Error", "Response is null or unsuccessful.");
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {

            }
        });
    }
    private void setClick(){
        btn_back.setOnClickListener(v->{
            finish();
        });
        btnAdd.setOnClickListener(v-> {
            String nameCategory = edtCategoryName.getText().toString().trim();
            if (nameCategory.length() == 0) {
                Toast.makeText(this, "Category name cannot be blank", Toast.LENGTH_SHORT).show();
            } else {
                apiService.addCategory(nameCategory).enqueue(new Callback<OneCategoryResponse>() {
                    @Override
                    public void onResponse(Call<OneCategoryResponse> call, Response<OneCategoryResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            OneCategoryResponse body = response.body();
                            if (body.isStatus() && body.getBody() != null) {
                                lstCategory.add(new Category(body.getBody().getId(), body.getBody().getName()));
                                adapter.notifyItemInserted(lstCategory.size() - 1);
                                recyclerView.scrollToPosition(lstCategory.size() - 1);
                                edtCategoryName.setText("");
                            } else {
                                Log.e("API Error", "Category list is empty or null.");
                            }
                        } else {
                            Log.e("API Error", "Response is null or unsuccessful.");
                        }
                    }

                    @Override
                    public void onFailure(Call<OneCategoryResponse> call, Throwable t) {
                        Toast.makeText(Activity_Manage_Category.this, "Server not responding", Toast.LENGTH_SHORT).show();

                    }
                });
            }

        });
    }
}
