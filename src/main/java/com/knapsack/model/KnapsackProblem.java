package com.knapsack.model;

import java.util.ArrayList;
import java.util.List;

public class KnapsackProblem {
    private double capacity;
    private List<ItemSet> itemSets;

    public KnapsackProblem(double capacity) {
        this.capacity = capacity;
        this.itemSets = new ArrayList<>();
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public List<ItemSet> getItemSets() {
        return itemSets;
    }

    public void setItemSets(List<ItemSet> itemSets) {
        this.itemSets = itemSets;
    }

    public void addItemSet(ItemSet itemSet) {
        itemSets.add(itemSet);
    }

    public int getItemCount() {
        return itemSets.size();
    }

    @Override
    public String toString() {
        return "KnapsackProblem{capacity=" + capacity + ", itemSetCount=" + itemSets.size() + "}";
    }
}
