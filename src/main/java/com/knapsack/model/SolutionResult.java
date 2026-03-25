package com.knapsack.model;

import java.util.ArrayList;
import java.util.List;

public class SolutionResult {
    private double totalValue;
    private double totalWeight;
    private List<SelectedItem> selectedItems;
    private long solveTime;
    private String algorithm;

    public SolutionResult() {
        this.selectedItems = new ArrayList<>();
        this.solveTime = 0;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public List<SelectedItem> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<SelectedItem> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public void addSelectedItem(SelectedItem item) {
        selectedItems.add(item);
    }

    public long getSolveTime() {
        return solveTime;
    }

    public void setSolveTime(long solveTime) {
        this.solveTime = solveTime;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String toString() {
        return "SolutionResult{totalValue=" + totalValue + ", totalWeight=" + totalWeight + 
               ", solveTime=" + solveTime + "ms, algorithm='" + algorithm + 
               "', selectedItems=" + selectedItems.size() + "}";
    }
}
