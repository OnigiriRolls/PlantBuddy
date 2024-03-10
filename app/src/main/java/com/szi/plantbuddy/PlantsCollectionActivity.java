package com.szi.plantbuddy;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.szi.plantbuddy.pagination.PlantViewModel;
import com.szi.plantbuddy.ui.PlantAdapter;
import com.szi.plantbuddy.ui.PlantComparator;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;


public class PlantsCollectionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants_collection);

        PlantViewModel viewModel = new ViewModelProvider(this).get(PlantViewModel.class);

        PlantAdapter pagingAdapter = new PlantAdapter(new PlantComparator());
        RecyclerView recyclerView = findViewById(R.id.vPlants);
        recyclerView.setAdapter(pagingAdapter);

        viewModel.getFlowable(this)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(pagingData -> pagingAdapter.submitData(getLifecycle(), pagingData));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
}