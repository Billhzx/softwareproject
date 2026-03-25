package com.knapsack.algorithm;

import com.knapsack.model.ItemSet;
import com.knapsack.model.KnapsackProblem;
import com.knapsack.model.SelectedItem;
import com.knapsack.model.SolutionResult;

import java.util.ArrayList;
import java.util.List;

public class DynamicProgrammingSolver {
    private static final int PRECISION_FACTOR = 100;
    private static final int MAX_CAPACITY = 100000; // 最大容量限制

    public SolutionResult solve(KnapsackProblem problem) {
        long startTime = System.currentTimeMillis();
        SolutionResult result = new SolutionResult();
        result.setAlgorithm("Dynamic Programming");

        List<ItemSet> itemSets = problem.getItemSets();
        int n = itemSets.size();
        double capacity = problem.getCapacity();

        int intCapacity = (int) (capacity * PRECISION_FACTOR);
        
        // 如果容量太大，使用贪心算法作为备选
        if (intCapacity > MAX_CAPACITY) {
            return solveWithGreedy(problem, result, startTime);
        }

        // 使用滚动数组优化内存
        int[] dp = new int[intCapacity + 1];
        int[][] choice = new int[n + 1][intCapacity + 1];
        int[][] choiceItem = new int[n + 1][intCapacity + 1];

        for (int i = 1; i <= n; i++) {
            ItemSet itemSet = itemSets.get(i - 1);
            // 从后往前遍历，避免覆盖
            for (int j = intCapacity; j >= 0; j--) {
                for (int k = 0; k < 3; k++) {
                    int weight = (int) (itemSet.getItem(k).getWeight() * PRECISION_FACTOR);
                    int value = (int) (itemSet.getItem(k).getValue() * PRECISION_FACTOR);

                    if (j >= weight) {
                        int newValue = dp[j - weight] + value;
                        if (newValue > dp[j]) {
                            dp[j] = newValue;
                            choice[i][j] = 1;
                            choiceItem[i][j] = k;
                        }
                    }
                }
            }
        }

        int maxValue = dp[intCapacity];
        result.setTotalValue(maxValue / (double) PRECISION_FACTOR);

        int currentWeight = intCapacity;
        List<SelectedItem> selectedItems = new ArrayList<>();

        for (int i = n; i > 0; i--) {
            if (choice[i][currentWeight] == 1) {
                int itemIndex = choiceItem[i][currentWeight];
                ItemSet itemSet = itemSets.get(i - 1);
                double weight = itemSet.getItem(itemIndex).getWeight();
                double value = itemSet.getItem(itemIndex).getValue();

                selectedItems.add(new SelectedItem(
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
    
    // 贪心算法（用于大数据集）
    private SolutionResult solveWithGreedy(KnapsackProblem problem, SolutionResult result, long startTime) {
        result.setAlgorithm("Greedy Algorithm (Capacity too large for DP)");
        
        List<ItemSet> itemSets = problem.getItemSets();
        double capacity = problem.getCapacity();
        double remainingCapacity = capacity;
        
        List<SelectedItem> selectedItems = new ArrayList<>();
        double totalValue = 0;
        double totalWeight = 0;
        
        // 对每个项集，选择价值重量比最高的物品
        for (ItemSet itemSet : itemSets) {
            int bestIndex = 0;
            double bestRatio = 0;
            
            for (int k = 0; k < 3; k++) {
                double weight = itemSet.getItem(k).getWeight();
                double value = itemSet.getItem(k).getValue();
                double ratio = value / weight;
                
                if (ratio > bestRatio && weight <= remainingCapacity) {
                    bestRatio = ratio;
                    bestIndex = k;
                }
            }
            
            double selectedWeight = itemSet.getItem(bestIndex).getWeight();
            double selectedValue = itemSet.getItem(bestIndex).getValue();
            
            if (selectedWeight <= remainingCapacity) {
                selectedItems.add(new SelectedItem(
                        itemSet.getId(),
                        bestIndex,
                        selectedWeight,
                        selectedValue
                ));
                totalValue += selectedValue;
                totalWeight += selectedWeight;
                remainingCapacity -= selectedWeight;
            }
        }
        
        result.setTotalValue(totalValue);
        result.setTotalWeight(totalWeight);
        result.setSelectedItems(selectedItems);
        
        long endTime = System.currentTimeMillis();
        result.setSolveTime(endTime - startTime);
        
        return result;
    }
}
