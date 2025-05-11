package com.example.doancuoiky.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doancuoiky.Config.Config;
import com.example.doancuoiky.Model.Product;
import com.example.doancuoiky.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;

    public interface OnProductActionListener {
        void onClickImage(Product product);
        void onIncrease(Product product);
        void onDecrease(Product product);
        void onDelete(Product product);
    }

    private OnProductActionListener listener;

    public ProductAdapter(Context context, List<Product> productList, OnProductActionListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.textName.setText(product.getName());
        holder.textQuantity.setText(String.valueOf(product.getQuantity()));
        double price = product.getPrice();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.textPrice.setText(formatter.format(price));

        Glide.with(context)
                .load(Config.URL_API_IMAGE+product.getImage())
                .placeholder(R.drawable.triangle_exclamation_solid) // ảnh tạm khi đang load
                .error(R.drawable.triangle_exclamation_solid)       // ảnh lỗi nếu không load được
                .into(holder.imageProduct);

        holder.btnIncrease.setOnClickListener(v -> listener.onIncrease(product));
        holder.btnDecrease.setOnClickListener(v -> listener.onDecrease(product));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(product));
        holder.imageProduct.setOnClickListener(v->listener.onClickImage(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView textName, textQuantity, textPrice;
        ImageButton btnIncrease, btnDecrease, btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.image_product);
            textName = itemView.findViewById(R.id.text_product_name);
            textQuantity = itemView.findViewById(R.id.text_quantity);
            textPrice = itemView.findViewById(R.id.text_price);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
