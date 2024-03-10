package com.szi.plantbuddy.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.szi.plantbuddy.model.Plant;

import org.jetbrains.annotations.NotNull;

public class PlantAdapter extends PagingDataAdapter<Plant, PlantViewHolder> {
    public PlantAdapter(@NotNull DiffUtil.ItemCallback<Plant> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PlantViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant item = getItem(position);
        holder.bind(item);
    }
}
