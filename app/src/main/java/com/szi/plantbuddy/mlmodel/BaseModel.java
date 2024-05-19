package com.szi.plantbuddy.mlmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.szi.plantbuddy.MainActivity;
import com.szi.plantbuddy.exception.ModelException;
import com.szi.plantbuddy.util.ImageUtils;

import org.tensorflow.lite.gpu.CompatibilityList;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class BaseModel<T> implements IModelRunner {
    private final static int MAX_RESULTS = 5;
    private final AtomicReference<T> modelRef = new AtomicReference<>();

    protected abstract T createModelInstance(Context context, Model.Options options) throws IOException;

    protected abstract TensorBuffer runInference(T model, TensorImage image);

    @Override
    public List<FlowerResult> runModel(MainActivity mainActivity, Bitmap imageBitmap, List<FlowerLabel> labels) throws ModelException {
        initModel(mainActivity);
        return analyze(imageBitmap, labels);
    }

    private void initModel(MainActivity mainActivity) {
        try (CompatibilityList compatList = new CompatibilityList()) {

            Supplier<Model.Options> optionsSupplier = () -> {
                if (compatList.isDelegateSupportedOnThisDevice()) {
                    Log.d("debug", "This device is GPU Compatible ");
                    return new Model.Options.Builder()
                            .setDevice(Model.Device.GPU)
                            .build();
                } else {
                    Log.d("debug", "This device is GPU Incompatible ");
                    return new Model.Options.Builder()
                            .setNumThreads(4)
                            .build();
                }
            };

            modelRef.updateAndGet(model -> {
                try {
                    return model == null ? createModelInstance(mainActivity.getApplicationContext(), optionsSupplier.get()) : model;
                } catch (IOException e) {
                    Log.d("debug", "update and get model ref error ", e);
                }
                return null;
            });
        }
    }

    private List<FlowerResult> analyze(Bitmap imageBitmap, List<FlowerLabel> labels) throws ModelException {
        TensorImage image = TensorImage.fromBitmap(imageBitmap);
        image = ImageUtils.processTensorImage(image);

        T model = modelRef.get();

        if (model == null) {
            throw new ModelException("Model was not initialised successfully!");
        }

        Log.d("debug", "START inference");
        Instant start = Instant.now();
        TensorBuffer output = runInference(model, image);
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        Log.d("debug", "Time elapsed: " + timeElapsed);
        Log.d("debug", "FINISH inference");

        return processOutput(output, labels);
    }

    private List<FlowerResult> processOutput(TensorBuffer output, List<FlowerLabel> labels) {
        float[] outputFeature = output.getFloatArray();
        List<FlowerResult> results = new ArrayList<>();
        for (int i = 0; i < outputFeature.length; i++) {
            results.add(new FlowerResult(labels.get(i).toString(), outputFeature[i] * 100));
        }
        return results.stream().sorted(Comparator.comparingDouble(FlowerResult::getProbability).reversed()).limit(MAX_RESULTS).collect(Collectors.toList());
    }
}

