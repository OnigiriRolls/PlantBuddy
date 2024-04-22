package com.szi.plantbuddy.mlmodel;

import java.util.Objects;

public class FlowerLabel {
    private String name;
    private Integer label;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLabel() {
        return label;
    }

    public void setLabel(Integer label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlowerLabel that = (FlowerLabel) o;
        return Objects.equals(name, that.name) && Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, label);
    }
}
