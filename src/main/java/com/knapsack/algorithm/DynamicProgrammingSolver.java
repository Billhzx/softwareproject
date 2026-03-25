package com.knapsack.algorithm;

import com.knapsack.model.ItemSet;
import com.knapsack.model.KnapsackProblem;
import com.knapsack.model.SelectedItem;
import com.knapsack.model.SolutionResult;

import java.util.ArrayList;
import java.util.List;

public class DynamicProgrammingSolver {
    private static final int PRECISION_FACTOR = 100;

    public SolutionResult solve(KnapsackProblem problem) {
        long startTime = System.currentTimeMillis();
        SolutionResult result = new SolutionResult();
        result.setAlgorithm("Dynamic Programming");

        List<ItemSet> itemSets = problem.getItemSets();
        int n = itemSets.size();
        double capacity = problem.getCapacity();

        int intCapacity = (int) (capacity * PRECISION_FACTOR);

        int[][] dp = new int[n + 1][intCapacity + 1];
        int[][][] choice = new int[n + 1][intCapacity + 1][2];

        for (int i = 1; i <= n; i++) {
            ItemSet itemSet = itemSets.get(i - 1);
            for (int j = 0; j <= intCapacity; j++) {
                dp[i][j] = dp[i - 1][j];
                choice[i][j][0] = 0;
                choice[i][j][1] = -1;

                for (int k = 0; k < 3; k++) {
                    int weight = (int) (itemSet.getItem(k).getWeight() * PRECISION_FACTOR);
                    int value = (int) (itemSet.getItem(k).getValue() * PRECISION_FACTOR);

                    if (j >= weight) {
                        int newValue = dp[i - 1][j - weight] + value;
                        if (newValue > dp[i][j]) {
                            dp[i][j] = newValue;
                            choice[i][j][0] = 1;
                            choice[i][j][1] = k;
                        }
                    }
                }
            }
        }

        int maxValue = dp[n][intCapacity];
        result.setTotalValue(maxValue / (double) PRECISION_FACTOR);

        int currentWeight = intCapacity;
        List<SelectedItem> selectedItems = new ArrayList<>();

        for (int i = n; i > 0; i--) {
            if (choice[i][currentWeight][0] == 1) {
                int itemIndex = choice[i][currentWeight][1];
                ItemSet itemSet = itemSets.get(i - 1);
                double weight = itemSet.getItem(itemIndex).getWeight();
                double value = itemSet.getItem(itemIndex).getValue();

                selectedItems.add(new SelectedItem
                        itemSet.getId(),
                        itemIndex,
                        weight,
                        value
                ));

                currentWeight -= (int) (weight * PRECISION_FACTOR);
            }
        }

        double totalWeight = 0;
        for (SelectedItem item : selectedItems) {
            totalWeight += item.getWeight();
        }
        result.setTotalWeight(totalWeight);

        result.setSelectedItems(selectedItems);

        long endTime = System.currentTimeMillis();
        result.setSolveTime(endTime - startTime);

        return result;
    }
}
