package com.knapsack.model;

public class Item {
    private int id;
    private double weight;
    private double value;

    public Item(int id, double weight, double value) {
        this.id = id;
        this.weight = weight;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValueToWeightRatio() {
        if (weight == 0) {
            return 0;
        }
        return value / weight;
    }

    @Override
    public String toString() {
        return "Item{id=" + id + ", weight=" + weight + ", value=" + value + "}";
    }
}
