package com.knapsack.util;

import com.knapsack.model.ItemSet;

import java.util.Comparator;
import java.util.List;

public class DataSorter {
    public static void sortByThirdItemValueToWeightRatio(List<ItemSet> itemSets) {
        itemSets.sort((set1, set2) -> {
            double ratio1 = set1.getThirdItemValueToWeightRatio();
            double ratio2 = set2.getThirdItemValueToWeightRatio();
            return Double.compare(ratio2, ratio1);
        });
    }

    public static void sortByTotalValue(List<ItemSet> itemSets) {
        itemSets.sort((set1, set2) -> {
            double value1 = set1.getItem(2).getValue();
            double value2 = set2.getItem(2).getValue();
            return Double.compare(value2, value1);
        });
    }

    public static void sortByTotalWeight(List<ItemSet> itemSets) {
        itemSets.sort((set1, set2) -> {
            double weight1 = set1.getItem(2).getWeight();
            double weight2 = set2.getItem(2).getWeight();
            return Double.compare(weight1, weight2);
        });
    }
}
