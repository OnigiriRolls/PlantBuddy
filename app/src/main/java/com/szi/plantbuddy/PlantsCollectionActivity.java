package com.szi.plantbuddy;

import android.os.Bundle;

import androidx.lifecycle.ViewModelKt;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;
import androidx.recyclerview.widget.RecyclerView;

import com.szi.plantbuddy.model.Plant;
import com.szi.plantbuddy.pagination.PlantPagingSource;
import com.szi.plantbuddy.pagination.PlantViewModel;
import com.szi.plantbuddy.ui.PlantAdapter;
import com.szi.plantbuddy.ui.PlantComparator;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;


import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class PlantsCollectionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants_collection);

        PlantViewModel viewModel =new ViewModelProvider(this)
                .get(PlantViewModel.class);

        PlantAdapter pagingAdapter = new PlantAdapter(new PlantComparator());
        RecyclerView recyclerView = findViewById(
                R.id.vPlants);
        recyclerView.setAdapter(pagingAdapter);

        viewModel.getFlowable(this)
                // Using AutoDispose to handle subscription lifecycle.
                // See: https://github.com/uber/AutoDispose.
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(pagingData -> pagingAdapter.submitData(lifecycle, pagingData));
    }
}