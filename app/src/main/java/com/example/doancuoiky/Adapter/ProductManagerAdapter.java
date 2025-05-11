package com.example.doancuoiky.Adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doancuoiky.Config.Config;
import com.example.doancuoiky.Model.ProductManager;
import com.example.doancuoiky.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductManagerAdapter extends RecyclerView.Adapter<ProductManagerAdapter.ProductViewHolder> {

    private List<ProductManager> productManagerList;
    private OnToggleStatusListener onToggleStatusListener;

    // Constructor
    public ProductManagerAdapter(List<ProductManager> productManagerList, OnToggleStatusListener onToggleStatusListener) {
        this.productManagerList = productManagerList;
        this.onToggleStatusListener = onToggleStatusListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_manager, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductManager productManager = productManagerList.get(position);

        // Bind data
        holder.tvProductName.setText(productManager.getName());
        holder.tvCreatedDate.setText("Created date: " + productManager.getCreatedDate());
        holder.tvPrice.setText("Price: " + productManager.getPrice() );
        holder.tvStock.setText("Stock remaining: " + productManager.getStock());
        holder.tvStatus.setText("Status: " + (productManager.isVisible() ? "Visible" : "Hidden"));
        holder.btnToggleStatus.setText(productManager.isVisible() ? "Hide" : "Show");
        // Set color based on visibility status
        if (productManager.isVisible()) {
            holder.tvStatus.setTextColor(Color.parseColor("#018786")); // Green for "Visible"
            holder.btnToggleStatus.setTextColor(Color.WHITE); // White text for button
            holder.btnToggleStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6200EE"))); // Purple background for "Hide" button
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#D32F2F")); // Red for "Hidden"
            holder.btnToggleStatus.setTextColor(Color.WHITE); // White text for button
            holder.btnToggleStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9E9E9E"))); // Gray background for "Show" button
        }
        Glide.with(holder.itemView.getContext())
                .load(Config.URL_API_IMAGE+productManager.getImageResId())
                .placeholder(R.drawable.triangle_exclamation_solid)
                .error(R.drawable.triangle_exclamation_solid)
                .into(holder.imgProduct);
        // Handle toggle button click
        holder.btnToggleStatus.setOnClickListener(v -> {
            onToggleStatusListener.onToggleStatus(productManager);
        });
        holder.imgProduct.setOnClickListener(v->{
            onToggleStatusListener.clickImage(productManager);
        });
    }

    @Override
    public int getItemCount() {
        return productManagerList.size();
    }

    // ViewHolder class
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvCreatedDate, tvPrice, tvStock, tvStatus;
        Button btnToggleStatus;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvCreatedDate = itemView.findViewById(R.id.tvCreatedDate);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStock = itemView.findViewById(R.id.tvStock);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnToggleStatus = itemView.findViewById(R.id.btnToggleStatus);
        }
    }

    // Interface to handle toggle status
    public interface OnToggleStatusListener {
        void onToggleStatus(ProductManager productManager);
        void clickImage(ProductManager productManager);
    }
}
