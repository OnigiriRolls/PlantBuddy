package com.szi.plantbuddy.mlmodel;

import android.graphics.Bitmap;
import android.util.Log;

import com.szi.plantbuddy.MainActivity;
import com.szi.plantbuddy.exception.ModelException;
import com.szi.plantbuddy.util.ImageUtils;

import org.tensorflow.lite.gpu.CompatibilityList;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.model.Model;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FlowerModel {
    private final AtomicReference<com.szi.plantbuddy.ml.Model93001> flowerModelRef = new AtomicReference<>();

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

            flowerModelRef.updateAndGet(model -> {
                try {
                    return model == null ? com.szi.plantbuddy.ml.Model93001.newInstance(mainActivity.getApplicationContext(), optionsSupplier.get()) : model;
                } catch (IOException e) {
                    Log.d("debug", "update and get model ref error ");
                }
                return null;
            });
        }
    }

    private List<FlowerResult> analyze(Bitmap imageBitmap, List<FlowerLabel> labels) throws ModelException {
        TensorImage image = TensorImage.fromBitmap(imageBitmap);
        image = ImageUtils.processTensorImage(image);

        com.szi.plantbuddy.ml.Model93001 flowerModel = flowerModelRef.get();

        if (flowerModel == null) {
            throw new ModelException("Model was not initialised successfully!");
        }

        Log.d("debug", "START inference");
        Instant start = Instant.now();
        com.szi.plantbuddy.ml.Model93001.Outputs outputs = flowerModel.process(image.getTensorBuffer());
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        Log.d("debug", "Time elapsed" + timeElapsed);
        Log.d("debug", "FINISH inference");

        float[] outputFeature = outputs.getOutputFeature0AsTensorBuffer().getFloatArray();

        return getFinalResults(outputFeature, labels);
    }

    private List<FlowerResult> getFinalResults(float[] outputFeature, List<FlowerLabel> labels) {
        List<FlowerResult> results = new ArrayList<>();
        for (int i = 0; i < outputFeature.length; i++) {
            results.add(new FlowerResult(labels.get(i).toString(), outputFeature[i] * 100));
        }

        return results.stream().sorted(Comparator.comparingDouble(FlowerResult::getProbability).reversed()).collect(Collectors.toList());
    }
}
