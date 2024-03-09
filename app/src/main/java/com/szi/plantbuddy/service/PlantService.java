package com.szi.plantbuddy.service;

import androidx.appcompat.app.AppCompatActivity;

import org.chromium.net.CronetEngine;
import org.chromium.net.UrlRequest;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlantService {
    private static final String GET_PLANTS_URL = "https://perenual.com/api/species-list?key=sk-HBCx65ec58267c0464513";
    private static PlantService instance;
    private static CronetEngine cronetEngine;
    private static Executor executor;

    private PlantService() {
    }

    public static synchronized PlantService getInstance(AppCompatActivity activity) {
        if (instance == null) {
            CronetEngine.Builder myBuilder = new CronetEngine.Builder(activity.getApplicationContext());
            cronetEngine = myBuilder.build();
            executor = Executors.newSingleThreadExecutor();
            instance = new PlantService();
        }
        return instance;
    }

    public void getPlants() {
        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                GET_PLANTS_URL, new UrlRequestCallbackHandler(), executor);

        UrlRequest request = requestBuilder.build();
        request.start();
    }
}
