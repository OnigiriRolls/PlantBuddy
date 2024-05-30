package com.szi.plantbuddylite.mlmodel;

public class CountedSum {
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
