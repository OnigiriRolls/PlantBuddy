package com.szi.plantbuddy.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.szi.plantbuddy.R;
import com.szi.plantbuddy.model.Plant;

public class PlantViewHolder extends RecyclerView.ViewHolder {

    public PlantViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public static PlantViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plant_item, parent, false);
        return new PlantViewHolder(view);
    }

    public void bind(Plant plant) {
        TextView plantNameTextView = itemView.findViewById(R.id.tPlantName);
        ImageView imageView = itemView.findViewById(R.id.plantImage);
        if (plant != null) {
            plantNameTextView.setText(plant.getCommonName());
            imageView.setImageBitmap(plant.getImage().getThumbnail());
        }
    }
}
