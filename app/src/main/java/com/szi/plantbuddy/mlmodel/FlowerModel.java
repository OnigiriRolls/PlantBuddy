package com.szi.plantbuddy.mlmodel;

import android.graphics.Bitmap;
import android.util.Log;

import com.szi.plantbuddy.MainActivity;
import com.szi.plantbuddy.exception.ModelException;

import org.tensorflow.lite.gpu.CompatibilityList;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.support.model.Model;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FlowerModel {
    private final AtomicReference<com.szi.plantbuddy.ml.FlowerModel> flowerModelRef = new AtomicReference<>();

    public List<String> runModel(MainActivity mainActivity, Bitmap imageBitmap) throws ModelException {
        initModel(mainActivity);
        return analyze(imageBitmap);
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
                    return model == null ? com.szi.plantbuddy.ml.FlowerModel.newInstance(mainActivity.getApplicationContext(), optionsSupplier.get()) : model;
                } catch (IOException e) {
                    Log.d("debug", "update and get model ref error ");
                }
                return null;
            });
        }
    }

    private List<String> analyze(Bitmap imageBitmap) throws ModelException {
        TensorImage image = TensorImage.fromBitmap(imageBitmap);
        com.szi.plantbuddy.ml.FlowerModel flowerModel = flowerModelRef.get();

        if (flowerModel == null) {
            throw new ModelException("Model was not initialised successfully!");
        }

        List<Category> outputs = flowerModel.process(image)
                .getProbabilityAsCategoryList()
                .stream()
                .sorted(Comparator.comparing(Category::getScore).reversed())
                .collect(Collectors.toList());

        return getFinalResults(outputs);
    }

    private List<String> getFinalResults(List<Category> outputs) throws ModelException {
        List<String> labels = getLabelsFromOutputs(outputs);
        List<String> scores = getScoresFromOutputs(outputs);

        if (labels == null || scores == null || labels.size() != scores.size()) {
            throw new ModelException("Labels or/and scores are incorrect!");
        }

        List<String> results = combineLabelsAndScores(labels, scores);
        if (results == null || results.size() == 0)
            throw new ModelException("Results list is null or empty!");

        return results;
    }

    private List<String> combineLabelsAndScores(List<String> labels, List<String> scores) {
        return IntStream.range(0, labels.size())
                .mapToObj(i -> labels.get(i) + "  " + scores.get(i))
                .collect(Collectors.toList());

    }

    private List<String> getLabelsFromOutputs(List<Category> outputs) {
        return outputs.stream()
                .map(Category::getLabel)
                .collect(Collectors.toList());
    }

    private List<String> getScoresFromOutputs(List<Category> outputs) {
        return outputs.stream()
                .map(Category::getScore)
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

}