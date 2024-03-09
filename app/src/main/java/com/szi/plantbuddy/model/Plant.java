package com.szi.plantbuddy.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
}
