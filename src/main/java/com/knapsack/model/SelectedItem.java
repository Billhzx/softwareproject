package com.knapsack.model;

public class SelectedItem {
    private int itemSetId;
    private int itemIndex;
    private double weight;
    private double value;

    public SelectedItem(int itemSetId, int itemIndex, double weight, double value) {
        this.itemSetId = itemSetId;
        this.itemIndex = itemIndex;
        this.weight = weight;
        this.value = value;
    }

    public int getItemSetId() {
        return itemSetId;
    }

    public void setItemSetId(int itemSetId) {
        this.itemSetId = itemSetId;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(int itemIndex) {
        this.itemIndex = itemIndex;
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

    @Override
    public String toString() {
        return "SelectedItem{itemSetId=" + itemSetId + ", itemIndex=" + itemIndex + 
               ", weight=" + weight + ", value=" + value + "}";
    }
}
