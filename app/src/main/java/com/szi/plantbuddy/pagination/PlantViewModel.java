package com.szi.plantbuddy.pagination;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.szi.plantbuddy.model.Plant;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class PlantViewModel extends AndroidViewModel {

    public PlantViewModel(Application application) {
        super(application);
    }

    public Flowable<PagingData<Plant>> getFlowable(AppCompatActivity activity) {
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer, Plant> pager = new Pager<>(
                new PagingConfig(30),
                () -> new PlantPagingSource(activity)
        );

        Flowable<PagingData<Plant>> flowable = PagingRx.getFlowable(pager);
        PagingRx.cachedIn(flowable, viewModelScope);
        return flowable;
    }
}
