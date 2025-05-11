package com.example.doancuoiky.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancuoiky.Model.Order;
import com.example.doancuoiky.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;
    private OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderHistoryAdapter(Context context, List<Order> orderList, OnOrderClickListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_bill_user, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.textOrderDate.setText("Date: " + order.getDate());
        holder.textOrderId.setText("Order code: #" + order.getOrderId());

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.textOrderTotal.setText("Total: " + formatter.format(order.getTotalPrice()));

        holder.textOrderStatus.setText(order.getStatusText());

        // Màu trạng thái
        switch (order.getStatus()) {
            case 0:
                holder.textOrderStatus.setTextColor(Color.GRAY); break;
            case 1:
                holder.textOrderStatus.setTextColor(Color.parseColor("#FFA000")); break;
            case 2:
                holder.textOrderStatus.setTextColor(Color.parseColor("#4CAF50")); break;
            case 3:
                holder.textOrderStatus.setTextColor(Color.RED); break;
        }

        holder.itemView.setOnClickListener(v -> listener.onOrderClick(order));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textOrderDate, textOrderId, textOrderTotal, textOrderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textOrderDate = itemView.findViewById(R.id.text_order_date);
            textOrderId = itemView.findViewById(R.id.text_order_id);
            textOrderTotal = itemView.findViewById(R.id.text_order_total);
            textOrderStatus = itemView.findViewById(R.id.text_order_status);
        }
    }
}