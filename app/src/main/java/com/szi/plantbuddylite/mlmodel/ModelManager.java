package com.szi.plantbuddylite.mlmodel;

import android.graphics.Bitmap;

import com.szi.plantbuddylite.MainActivity;
import com.szi.plantbuddylite.exception.ModelException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelManager {
    private final List<IModelRunner> modelRunners = new ArrayList<>();
    private final int MAX_ELEMENTS = 5;
    private final int MIN_COUNT = 2;

    public void addModelRunner(IModelRunner modelRunner) {
        modelRunners.add(modelRunner);
    }

    public List<String> runModels(MainActivity mainActivity, Bitmap imageBitmap, List<FlowerLabel> labels) throws ModelException {
        Map<String, CountedSum> countedLabels = new HashMap<>();

        for (IModelRunner modelRunner : modelRunners) {
            List<FlowerResult> results = modelRunner.runModel(mainActivity, imageBitmap, labels);
            countResults(countedLabels, results);
        }

        List<Map.Entry<String, CountedSum>> countedLabelsOrdered = sortCountedLabels(countedLabels);
        int n = getNumberOfResultsToDisplay(countedLabelsOrdered);
        return getLabelsToDisplay(countedLabelsOrdered, n);
    }

    private List<String> getLabelsToDisplay(List<Map.Entry<String, CountedSum>> countedLabelsOrdered, int n) {
        Map.Entry<String, CountedSum> bestResult = countedLabelsOrdered.get(0);
        if (bestResult.getValue().getCount() < MIN_COUNT) {
            return countedLabelsOrdered.stream()
                    .limit(n)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
        return Collections.singletonList(bestResult.getKey());
    }

    private int getNumberOfResultsToDisplay(List<Map.Entry<String, CountedSum>> countedLabelsOrdered) {
        int i = 0, lastSum = 0;
        while (i < MAX_ELEMENTS || (i < countedLabelsOrdered.size() && lastSum == countedLabelsOrdered.get(i).getValue().getSum() && lastSum > 0)) {
            lastSum = countedLabelsOrdered.get(i).getValue().getSum();
            i++;
        }

        return i;
    }

    private List<Map.Entry<String, CountedSum>> sortCountedLabels(Map<String, CountedSum> countedLabels) {
        return countedLabels.entrySet().stream()
                .sorted(Comparator.comparingInt((Map.Entry<String, CountedSum> e) -> e.getValue().getSum()).reversed())
                .collect(Collectors.toList());
    }

    private void countResults(Map<String, CountedSum> countedLabels, List<FlowerResult> results) {
        results.forEach(r -> {
            String label = r.getFlowerLabel();
            int probability = r.getProbability().intValue();
            boolean isHighProbability = probability >= 90;

            countedLabels.merge(label, new CountedSum(probability, isHighProbability ? 1 : 0),
                    (oldEntry, newEntry) -> {
                        oldEntry.addSum(probability);
                        if (isHighProbability) {
                            oldEntry.incrementCount();
                        }
                        return oldEntry;
                    });
        });
    }
}

