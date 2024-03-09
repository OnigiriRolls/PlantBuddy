package com.szi.plantbuddy.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.szi.plantbuddy.model.Plant;

public class PlantComparator extends DiffUtil.ItemCallback<Plant>{
    @Override
    public boolean areItemsTheSame(@NonNull Plant oldItem,
                                   @NonNull Plant newItem) {
        // Id is unique.
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Plant oldItem,
                                      @NonNull Plant newItem) {
        return oldItem.equals(newItem);
    }
}
