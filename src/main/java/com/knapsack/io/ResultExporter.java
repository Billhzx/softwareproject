package com.knapsack.io;

import com.knapsack.model.SolutionResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultExporter {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public void exportToTxt(String filePath, SolutionResult result) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            writer.write("========================================\n");
            writer.write("D{0-1}背包问题求解结果\n");
            writer.write("========================================\n");
            writer.write("导出时间: " + sdf.format(new Date()) + "\n");
            writer.write("算法: " + result.getAlgorithm() + "\n");
            writer.write("求解时间: " + result.getSolveTime() + " ms\n");
            writer.write("总价值: " + String.format("%.2f", result.getTotalValue()) + "\n");
            writer.write("总重量: " + String.format("%.2f", result.getTotalWeight()) + "\n");
            writer.write("========================================\n");
            writer.write("选中物品详情:\n");
            writer.write("----------------------------------------\n");
            writer.write(String.format("%-10s %-10s %-10s %-10s\n", "项集ID", "物品索引", "重量", "价值"));
            writer.write("----------------------------------------\n");

            for (int i = 0; i < result.getSelectedItems().size(); i++) {
                var item = result.getSelectedItems().get(i);
                writer.write(String.format("%-10d %-10d %-10.2f %-10.2f\n",
                        item.getItemSetId(), item.getItemIndex(),
                        item.getWeight(), item.getValue()));
            }

            writer.write("========================================\n");
            writer.write("共选中 " + result.getSelectedItems().size() + " 个物品\n");
            writer.write("========================================\n");
        }
    }

    public void exportToCsv(String filePath, SolutionResult result) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            writer.write("导出时间," + sdf.format(new Date()) + "\n");
            writer.write("算法," + result.getAlgorithm() + "\n");
            writer.write("求解时间(ms)," + result.getSolveTime() + "\n");
            writer.write("总价值," + String.format("%.2f", result.getTotalValue()) + "\n");
            writer.write("总重量," + String.format("%.2f", result.getTotalWeight()) + "\n");
            writer.write("\n");
            writer.write("项集ID,物品索引,重量,价值\n");

            for (var item : result.getSelectedItems()) {
                writer.write(String.format("%d,%d,%.2f,%.2f\n",
                        item.getItemSetId(), item.getItemIndex(),
                        item.getWeight(), item.getValue()));
            }
        }
    }
}
