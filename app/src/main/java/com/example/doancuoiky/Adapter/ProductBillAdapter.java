package com.example.doancuoiky.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doancuoiky.Config.Config;
import com.example.doancuoiky.Model.ProductBill;
import com.example.doancuoiky.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductBillAdapter extends RecyclerView.Adapter<ProductBillAdapter.ProductViewHolder> {

    private Context context;
    private List<ProductBill> productList;
    private OnItemClickListener listener;

    public ProductBillAdapter(Context context, List<ProductBill> productList,OnItemClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onImageClick(ProductBill product);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_bill, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductBill product = productList.get(position);
        holder.textProductName.setText(product.getName());
        holder.textQuantity.setText("quantity: " + product.getQuantity());
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.textPrice.setText(formatter.format(product.getPrice()));
        Glide.with(context)
                .load(Config.URL_API_IMAGE+product.getImage())
                .placeholder(R.drawable.triangle_exclamation_solid) // ảnh tạm khi đang load
                .error(R.drawable.triangle_exclamation_solid)       // ảnh lỗi nếu không load được
                .into(holder.imageProduct);
        holder.imageProduct.setOnClickListener(v->{listener.onImageClick(product);});
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textProductName, textQuantity, textPrice;
        ImageView imageProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textProductName = itemView.findViewById(R.id.text_product_name);
            textQuantity = itemView.findViewById(R.id.text_quantity);
            textPrice = itemView.findViewById(R.id.text_price);
            imageProduct = itemView.findViewById(R.id.image_product);
        }
    }
}