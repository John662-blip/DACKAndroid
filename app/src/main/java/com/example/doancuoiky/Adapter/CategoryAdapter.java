package com.example.doancuoiky.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancuoiky.Model.Category;
import com.example.doancuoiky.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;
    private int selectedPosition = 0;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }

    public CategoryAdapter(List<Category> categoryList, OnItemClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tabText;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tabText = itemView.findViewById(R.id.tab_recommend);
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tabText.setText(category.getName());

        // Set background and text color depending on selection
        if (position == selectedPosition) {
            holder.tabText.setBackgroundResource(R.drawable.tab_background_check);
            holder.tabText.setTextColor(Color.parseColor("#325A3E"));
        } else {
            holder.tabText.setBackgroundResource(R.drawable.tab_background);
            holder.tabText.setTextColor(Color.parseColor("#999898"));
        }

        holder.itemView.setOnClickListener(v -> {
            int previous = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previous);
            notifyItemChanged(selectedPosition);
            listener.onItemClick(category);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}

