package com.szi.plantbuddy.mlmodel;

import androidx.annotation.NonNull;

import java.util.Objects;

public class FlowerResult {
    private String flowerLabel;
    private Float probability;

    public FlowerResult(String flowerLabel, Float probability) {
        this.flowerLabel = flowerLabel;
        this.probability = probability;
    }

    public String getFlowerLabel() {
        return flowerLabel;
    }

    public void setFlowerLabel(String flowerLabel) {
        this.flowerLabel = flowerLabel;
    }

    public Float getProbability() {
        return probability;
    }

    public void setProbability(Float probability) {
        this.probability = probability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlowerResult that = (FlowerResult) o;
        return Objects.equals(flowerLabel, that.flowerLabel) && Objects.equals(probability, that.probability);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flowerLabel, probability);
    }

    @NonNull
    @Override
    public String toString() {
        return flowerLabel + " " + probability;
    }
}
