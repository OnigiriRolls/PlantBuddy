package com.szi.plantbuddy.mlmodel;

import android.graphics.Bitmap;
import android.util.Log;

import com.szi.plantbuddy.MainActivity;
import com.szi.plantbuddy.exception.ModelException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelManager {
    private final List<IModelRunner> modelRunners = new ArrayList<>();
    private final int MAX_ELEMENTS = 5;

    public void addModelRunner(IModelRunner modelRunner) {
        modelRunners.add(modelRunner);
    }

    public List<FlowerResult> runModels(MainActivity mainActivity, Bitmap imageBitmap, List<FlowerLabel> labels) throws ModelException {
        List<FlowerResult> combinedResults = new ArrayList<>();
        Map<String, CountedSum> countedLabels = new HashMap<>();

        for (IModelRunner modelRunner : modelRunners) {
            List<FlowerResult> results = modelRunner.runModel(mainActivity, imageBitmap, labels);
            combinedResults.addAll(results);
            countResults(countedLabels, results);
        }

        List<Map.Entry<String, CountedSum>> countedLabelsOrdered = countedLabels.entrySet().stream()
                .sorted(Comparator.comparingInt((Map.Entry<String, CountedSum> e) -> e.getValue().getSum()).reversed())
                .collect(Collectors.toList());

        int i = 0, lastSum = 0;
        while (i < MAX_ELEMENTS || (i < countedLabelsOrdered.size() && lastSum == countedLabelsOrdered.get(i).getValue().getSum())) {
            lastSum = countedLabelsOrdered.get(i).getValue().getSum();
            i++;
        }

        Log.d("debug", combinedResults.toString());

        return countedLabelsOrdered.stream()
                .limit(i)
                .map(e -> new FlowerResult(e.getKey(), (float) e.getValue().getCount()))
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

    private static class CountedSum {
        private int sum;
        private int count;

        public CountedSum(int sum, int count) {
            this.sum = sum;
            this.count = count;
        }

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void incrementCount() {
            count++;
        }

        public void addSum(int sum) {
            this.sum += sum;
        }
    }
}

