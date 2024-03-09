package com.szi.plantbuddy;

import android.os.Bundle;

import com.szi.plantbuddy.service.PlantService;

public class PlantsCollectionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants_collection);

        PlantService.getInstance(this).getPlants();
    }
}