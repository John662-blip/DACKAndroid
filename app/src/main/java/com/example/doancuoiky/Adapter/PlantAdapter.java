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
import com.example.doancuoiky.Model.PlantModel;
import com.example.doancuoiky.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private Context context;
    private List<PlantModel> plantList;

    public PlantAdapter(Context context, List<PlantModel> plantList) {
        this.context = context;
        this.plantList = plantList;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plant, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        PlantModel plant = plantList.get(position);
        holder.txtCategory.setText(plant.getName());
        holder.txtQuantity.setText("Còn lại : " + plant.getQuantity());
        holder.txtPrice.setText(plant.getPrice());
        Glide.with(holder.itemView.getContext())
                .load(Config.URL_API_IMAGE+plant.getUrlImage())
                .placeholder(R.drawable.triangle_exclamation_solid)
                .error(R.drawable.triangle_exclamation_solid)
                .into(holder.imgPlant);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(plant);
            }
        });
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public static class PlantViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPlant;
        TextView txtCategory, txtQuantity, txtPrice;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlant = itemView.findViewById(R.id.imgPlant);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtQuantity = itemView.findViewById(R.id.quantity);
            txtPrice = itemView.findViewById(R.id.txtPrice);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(PlantModel plant);
    }
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}