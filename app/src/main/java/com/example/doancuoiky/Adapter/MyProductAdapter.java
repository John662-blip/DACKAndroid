package com.example.doancuoiky.Adapter;

import android.content.Context;
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
import com.example.doancuoiky.Model.MyProduct;
import com.example.doancuoiky.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.MyProductViewHolder> {

    private Context context;
    private List<MyProduct> productList;
    private OnClickListener onClickListener;
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public interface OnClickListener {
        void onDeleteClick(MyProduct product);
        void onCLick(MyProduct product);
    }

    public MyProductAdapter(Context context, List<MyProduct> productList, OnClickListener onClickListener) {
        this.context = context;
        this.productList = productList;
        this.onClickListener = onClickListener;
    }

    public static class MyProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvPrice, tvCreatedDate, tvQuantity;
        Button btnDelete;

        public MyProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCreatedDate = itemView.findViewById(R.id.tvCreatedDate);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public MyProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_in_category, parent, false);
        return new MyProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductViewHolder holder, int position) {
        MyProduct product = productList.get(position);

//        holder.imgProduct.setImageResource(product.getImageResId());
        holder.tvProductName.setText(product.getName());
        holder.tvPrice.setText("Price: " +formatter.format( product.getPrice()));
        holder.tvCreatedDate.setText("Created: " + product.getCreatedDate());
        holder.tvQuantity.setText("Quantity: " + product.getQuantity());
        holder.imgProduct.setOnClickListener(v->onClickListener.onCLick(product));
        Glide.with(holder.itemView.getContext())
                .load(Config.URL_API_IMAGE+product.getImageResId())
                .placeholder(R.drawable.triangle_exclamation_solid)
                .error(R.drawable.triangle_exclamation_solid)
                .into(holder.imgProduct);
        holder.btnDelete.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onDeleteClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void removeProduct(MyProduct product) {
        int index = productList.indexOf(product);
        if (index != -1) {
            productList.remove(index);
            notifyItemRemoved(index);
        }
    }
}
