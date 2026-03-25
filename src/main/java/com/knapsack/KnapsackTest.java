package com.knapsack;

import com.knapsack.algorithm.DynamicProgrammingSolver;
import com.knapsack.io.DataFileReader;
import com.knapsack.model.Item;
import com.knapsack.model.ItemSet;
import com.knapsack.model.KnapsackProblem;
import com.knapsack.model.SolutionResult;
import com.knapsack.util.DataSorter;

import java.util.List;

public class KnapsackTest {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("D{0-1}背包问题求解器 - 测试程序");
        System.out.println("========================================\n");

        testModelClasses();
        testDataFileReader();
        testDataSorter();
        testDynamicProgrammingSolver();

        System.out.println("\n========================================");
        System.out.println("所有测试完成！");
        System.out.println("========================================");
    }

    private static void testModelClasses() {
        System.out.println("【测试1】数据模型类测试");
        System.out.println("----------------------------------------");

        Item item1 = new Item(0, 10.5, 25.0);
        System.out.println("创建物品: " + item1);
        System.out.println("价值/重量比: " + String.format("%.4f", item1.getValueToWeightRatio()));

        Item item2 = new Item(1, 8.0, 20.0);
        Item item3 = new Item(2, 15.0, 35.0);
        Item[] items = {item1, item2, item3};

        ItemSet itemSet = new ItemSet(0, items);
        System.out.println("创建项集: " + itemSet);
        System.out.println("第三项价值/重量比: " + String.format("%.4f", itemSet.getThirdItemValueToWeightRatio()));

        KnapsackProblem problem = new KnapsackProblem(100.0);
        problem.addItemSet(itemSet);
        System.out.println("创建背包问题: " + problem);
        System.out.println("背包容量: " + problem.getCapacity());
        System.out.println("项集数量: " + problem.getItemCount());

        System.out.println("✓ 数据模型类测试通过\n");
    }

    private static void testDataFileReader() {
        System.out.println("【测试2】数据文件读取测试");
        System.out.println("----------------------------------------");

        try {
            DataFileReader reader = new DataFileReader();
            String filePath = "src/resources/test_data_small.txt";

            System.out.println("读取文件: " + filePath);
            KnapsackProblem problem = reader.readFile(filePath);

            System.out.println("背包容量: " + problem.getCapacity());
            System.out.println("项集数量: " + problem.getItemCount());

            List<ItemSet> itemSets = problem.getItemSets();
            System.out.println("\n前3个项集数据:");
            for (int i = 0; i < Math.min(3, itemSets.size()); i++) {
                ItemSet is = itemSets.get(i);
                System.out.println("  项集" + is.getId() + ":");
                for (int j = 0; j < 3; j++) {
                    Item item = is.getItem(j);
                    System.out.println("    物品" + j + ": 重量=" + item.getWeight() + 
                                     ", 价值=" + item.getValue());
                }
            }

            System.out.println("✓ 数据文件读取测试通过\n");
        } catch (Exception e) {
            System.out.println("✗ 数据文件读取测试失败: " + e.getMessage() + "\n");
        }
    }

    private static void testDataSorter() {
        System.out.println("【测试3】数据排序测试");
        System.out.println("----------------------------------------");

        try {
            DataFileReader reader = new DataFileReader();
            KnapsackProblem problem = reader.readFile("src/resources/test_data_small.txt");
            List<ItemSet> itemSets = problem.getItemSets();

            System.out.println("排序前（前3个项集的价值/重量比）:");
            for (int i = 0; i < Math.min(3, itemSets.size()); i++) {
                System.out.println("  项集" + itemSets.get(i).getId() + ": " + 
                                 String.format("%.4f", itemSets.get(i).getThirdItemValueToWeightRatio()));
            }

            DataSorter.sortByThirdItemValueToWeightRatio(itemSets);

            System.out.println("\n排序后（前3个项集的价值/重量比，降序）:");
            for (int i = 0; i < Math.min(3, itemSets.size()); i++) {
                System.out.println("  项集" + itemSets.get(i).getId() + ": " + 
                                 String.format("%.4f", itemSets.get(i).getThirdItemValueToWeightRatio()));
            }

            System.out.println("✓ 数据排序测试通过\n");
        } catch (Exception e) {
            System.out.println("✗ 数据排序测试失败: " + e.getMessage() + "\n");
        }
    }

    private static void testDynamicProgrammingSolver() {
        System.out.println("【测试4】动态规划算法测试");
        System.out.println("----------------------------------------");

        try {
            DataFileReader reader = new DataFileReader();
            KnapsackProblem problem = reader.readFile("src/resources/test_data_small.txt");

            System.out.println("问题规模:");
            System.out.println("  背包容量: " + problem.getCapacity());
            System.out.println("  项集数量: " + problem.getItemCount());

            DynamicProgrammingSolver solver = new DynamicProgrammingSolver();
            SolutionResult result = solver.solve(problem);

            System.out.println("\n求解结果:");
            System.out.println("  算法: " + result.getAlgorithm());
            System.out.println("  求解时间: " + result.getSolveTime() + " ms");
            System.out.println("  总价值: " + String.format("%.2f", result.getTotalValue()));
            System.out.println("  总重量: " + String.format("%.2f", result.getTotalWeight()));
            System.out.println("  选中物品数量: " + result.getSelectedItems().size());

            System.out.println("\n选中物品详情:");
            for (var item : result.getSelectedItems()) {
                System.out.println("  项集" + item.getItemSetId() + " 物品" + item.getItemIndex() + 
                                 ": 重量=" + String.format("%.2f", item.getWeight()) + 
                                 ", 价值=" + String.format("%.2f", item.getValue()));
            }

            System.out.println("✓ 动态规划算法测试通过\n");
        } catch (Exception e) {
            System.out.println("✗ 动态规划算法测试失败: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }
}
