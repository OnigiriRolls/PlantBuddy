package com.szi.plantbuddy.pagination;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.szi.plantbuddy.model.Plant;
import com.szi.plantbuddy.model.PlantResponse;
import com.szi.plantbuddy.service.PlantService;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlantPagingSource extends RxPagingSource<Integer, Plant> {
    @NonNull
    private AppCompatActivity activity;

    public PlantPagingSource(@NonNull AppCompatActivity activity) {
        this.activity = activity;
    }

    @NotNull
    @Override
    public Single<LoadResult<Integer, Plant>> loadSingle(
            @NotNull LoadParams<Integer> params) {
        Integer nextPageNumber = params.getKey();
        if (nextPageNumber == null) {
            nextPageNumber = 1;
        }

        Integer finalNextPageNumber = nextPageNumber;
        return PlantService.getInstance(activity).getPlants(finalNextPageNumber)
                .subscribeOn(Schedulers.io())
                .map(this::toLoadResult)
                .onErrorReturn(LoadResult.Error::new)
                .firstOrError();
    }

    private LoadResult<Integer, Plant> toLoadResult(
            @NonNull PlantResponse response) {
        return new LoadResult.Page<>(
                response.getPlants(),
                null, // Only paging forward.
                response.getNextPageNumber(),
                LoadResult.Page.COUNT_UNDEFINED,
                LoadResult.Page.COUNT_UNDEFINED);
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NotNull PagingState<Integer, Plant> state) {
        Integer anchorPosition = state.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }

        LoadResult.Page<Integer, Plant> anchorPage = state.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            return null;
        }

        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            return prevKey + 1;
        }

        Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            return nextKey - 1;
        }

        return null;
    }
}
