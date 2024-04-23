package com.szi.plantbuddy.mlmodel;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class FlowerResult implements Parcelable {
    public static final Creator<FlowerResult> CREATOR = new Creator<FlowerResult>() {
        @Override
        public FlowerResult createFromParcel(Parcel in) {
            return new FlowerResult(in);
        }

        @Override
        public FlowerResult[] newArray(int size) {
            return new FlowerResult[size];
        }
    };
    private String flowerLabel;
    private Float probability;

    public FlowerResult(String flowerLabel, Float probability) {
        this.flowerLabel = flowerLabel;
        this.probability = probability;
    }

    protected FlowerResult(Parcel in) {
        probability = in.readFloat();
        flowerLabel = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeFloat(probability);
        dest.writeString(flowerLabel);
    }
}
