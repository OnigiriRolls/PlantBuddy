package com.szi.plantbuddy.mlmodel;

import android.graphics.Bitmap;

import com.szi.plantbuddy.MainActivity;
import com.szi.plantbuddy.exception.ModelException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModelManager {
    private final List<IModelRunner> modelRunners = new ArrayList<>();

    public void addModelRunner(IModelRunner modelRunner) {
        modelRunners.add(modelRunner);
    }

    public List<FlowerResult> runModels(MainActivity mainActivity, Bitmap imageBitmap, List<FlowerLabel> labels) throws ModelException {
        List<FlowerResult> combinedResults = new ArrayList<>();

        for (IModelRunner modelRunner : modelRunners) {
            combinedResults.addAll(modelRunner.runModel(mainActivity, imageBitmap, labels));
        }

//        return combinedResults.stream()
//                .sorted(Comparator.comparingDouble(FlowerResult::getProbability).reversed())
//                .collect(Collectors.toList());
        return combinedResults;
    }
}

