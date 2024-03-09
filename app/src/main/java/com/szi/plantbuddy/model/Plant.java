package com.szi.plantbuddy.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Plant {
    private int id;
    @SerializedName("common_name")
    private String commonName;
    @SerializedName("scientific_name")
    private List<String> scientificName;
    @SerializedName("other_name")
    private List<String> otherName;
    private String cycle;
    private String watering;
    private List<String> sunlight;
    @SerializedName("default_image")
    private PlantImage image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public List<String> getScientificName() {
        return scientificName;
    }

    public void setScientificName(List<String> scientificName) {
        this.scientificName = scientificName;
    }

    public List<String> getOtherName() {
        return otherName;
    }

    public void setOtherName(List<String> otherName) {
        this.otherName = otherName;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getWatering() {
        return watering;
    }

    public void setWatering(String watering) {
        this.watering = watering;
    }

    public List<String> getSunlight() {
        return sunlight;
    }

    public void setSunlight(List<String> sunlight) {
        this.sunlight = sunlight;
    }

    public PlantImage getImage() {
        return image;
    }

    public void setImage(PlantImage image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return id == plant.id && Objects.equals(commonName, plant.commonName) && Objects.equals(scientificName, plant.scientificName) && Objects.equals(otherName, plant.otherName) && Objects.equals(cycle, plant.cycle) && Objects.equals(watering, plant.watering) && Objects.equals(sunlight, plant.sunlight) && Objects.equals(image, plant.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, commonName, scientificName, otherName, cycle, watering, sunlight, image);
    }
}
