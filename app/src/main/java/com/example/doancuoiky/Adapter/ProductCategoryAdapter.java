package com.example.doancuoiky.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancuoiky.Model.Category;
import com.example.doancuoiky.R;

import java.util.List;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.ViewHolder> {

    private List<Category> categoryList;
    private OnProductCategoryActionListener listener;

    public interface OnProductCategoryActionListener {
        void onEdit(Category category);
        void onDelete(Category category);
        void onClick(Category category);
    }

    public ProductCategoryAdapter(List<Category> categoryList, OnProductCategoryActionListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    public void updateData(List<Category> newList) {
        categoryList.clear();
        categoryList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tvCategoryId.setText(String.valueOf(category.getId()));
        holder.tvCategoryName.setText(category.getName());

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(category));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(category));
        holder.tvCategoryId.setOnClickListener(v->listener.onClick(category));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryId, tvCategoryName;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryId = itemView.findViewById(R.id.tvCategoryId);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}