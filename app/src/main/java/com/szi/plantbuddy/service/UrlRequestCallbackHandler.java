package com.szi.plantbuddy.service;

import android.util.Log;

import com.google.gson.Gson;
import com.szi.plantbuddy.model.PlantResponse;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.nio.ByteBuffer;

import io.reactivex.rxjava3.core.ObservableEmitter;

public class UrlRequestCallbackHandler extends UrlRequest.Callback {
    private static final boolean shouldFollow = false;
    private static final String TAG = "debug";
    private final ObservableEmitter<PlantResponse> emitter;

    public UrlRequestCallbackHandler(ObservableEmitter<PlantResponse> emitter) {
        this.emitter = emitter;
    }


    @Override
    public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) {
        Log.i(TAG, "onRedirectReceived method called.");
        // You should call the request.followRedirect() method to continue
        // processing the request.
        if (shouldFollow) {
            request.followRedirect();
        } else {
            request.cancel();
        }
    }

    @Override
    public void onResponseStarted(UrlRequest request, UrlResponseInfo info) {
        Log.i(TAG, "onResponseStarted method called.");
        // You should call the request.read() method before the request can be
        // further processed. The following instruction provides a ByteBuffer object
        // with a capacity of 102400 bytes for the read() method. The same buffer
        // with data is passed to the onReadCompleted() method.
        request.read(ByteBuffer.allocateDirect(102400));
    }

    @Override
    public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
        Log.i(TAG, "onReadCompleted method called.");

        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        String jsonString = new String(bytes);

        Gson gson = new Gson();
        PlantResponse response = gson.fromJson(jsonString, PlantResponse.class);

        emitter.onNext(response);
        emitter.onComplete();
    }

    @Override
    public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
        Log.i(TAG, "onSucceeded method called.");
    }

    @Override
    public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
        Log.i(TAG, "onFailed method called.");
    }
}
