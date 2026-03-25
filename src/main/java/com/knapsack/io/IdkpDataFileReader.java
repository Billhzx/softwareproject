package com.knapsack.io;

import com.knapsack.model.Item;
import com.knapsack.model.ItemSet;
import com.knapsack.model.KnapsackProblem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IdkpDataFileReader {
    
    public List<KnapsackProblem> readIdkpFile(String filePath) throws IOException {
        List<KnapsackProblem> problems = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            KnapsackProblem currentProblem = null;
            List<Integer> profits = new ArrayList<>();
            List<Integer> weights = new ArrayList<>();
            int capacity = 0;
            int dimension = 0;
            boolean readingProfits = false;
            boolean readingWeights = false;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                
                // 跳过空行和注释行（只跳过以***开头的行，不是The开头的行）
                if (line.isEmpty() || line.startsWith("*")) {
                    continue;
                }
                
                // 检测新的实例开始
                if (line.startsWith("IDKP") || line.startsWith("SDKP") || line.startsWith("UDKP") || line.startsWith("WDKP")) {
                    // 如果已经有数据，保存上一个实例
                    if (currentProblem != null && !profits.isEmpty() && !weights.isEmpty()) {
                        buildProblem(currentProblem, profits, weights, dimension);
                        problems.add(currentProblem);
                    }
                    
                    // 创建新的实例
                    String problemName = line.replace(":", "").trim();
                    currentProblem = new KnapsackProblem(0);
                    profits.clear();
                    weights.clear();
                    readingProfits = false;
                    readingWeights = false;
                    continue;
                }
                
                // 读取维度信息和背包容量（IDKP格式：The dimension is d=3*100, the cubage of knapsack is 61500.）
                if (line.contains("dimension is d=") || line.contains("diemnsion is d=")) {
                    try {
                        // 解析维度 d=3*100
                        int dIndex = line.indexOf("d=");
                        String dimPart = line.substring(dIndex + 2);
                        if (dimPart.contains(",")) {
                            dimPart = dimPart.substring(0, dimPart.indexOf(","));
                        }
                        // 处理 3*100 格式
                        if (dimPart.contains("*")) {
                            String[] parts = dimPart.split("\\*");
                            dimension = Integer.parseInt(parts[1].trim());
                        } else {
                            dimension = Integer.parseInt(dimPart.trim());
                        }
                        
                        // 同时解析容量
                        if (line.contains("cubage of knapsack is")) {
                            int isIndex = line.indexOf("is", line.indexOf("cubage"));
                            String capPart = line.substring(isIndex + 2);
                            if (capPart.contains(".")) {
                                capPart = capPart.substring(0, capPart.indexOf("."));
                            }
                            capacity = Integer.parseInt(capPart.trim());
                            if (currentProblem != null) {
                                currentProblem.setCapacity(capacity);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("解析维度或容量失败: " + line + " - " + e.getMessage());
                    }
                    continue;
                }
                
                // 检测利润数据开始
                if (line.contains("profit of") && line.contains("are:")) {
                    readingProfits = true;
                    readingWeights = false;
                    continue;
                }
                
                // 检测重量数据开始
                if (line.contains("weight of") && line.contains("are:")) {
                    readingProfits = false;
                    readingWeights = true;
                    continue;
                }
                
                // 读取数据行
                if (readingProfits || readingWeights) {
                    String[] values = line.split(",");
                    for (String value : values) {
                        value = value.trim();
                        if (!value.isEmpty()) {
                            try {
                                int num = Integer.parseInt(value);
                                if (readingProfits) {
                                    profits.add(num);
                                } else if (readingWeights) {
                                    weights.add(num);
                                }
                            } catch (NumberFormatException e) {
                                // 忽略无法解析的数字
                            }
                        }
                    }
                }
            }
            
            // 保存最后一个实例
            if (currentProblem != null && !profits.isEmpty() && !weights.isEmpty()) {
                buildProblem(currentProblem, profits, weights, dimension);
                problems.add(currentProblem);
            }
        }
        
        return problems;
    }
    
    private void buildProblem(KnapsackProblem problem, List<Integer> profits, List<Integer> weights, int dimension) {
        // IDKP数据中，每3个物品组成一个项集
        int itemsPerSet = 3;
        int numSets = dimension / itemsPerSet;
        
        for (int i = 0; i < numSets && i * itemsPerSet < profits.size() && i * itemsPerSet < weights.size(); i++) {
            Item[] items = new Item[itemsPerSet];
            
            for (int j = 0; j < itemsPerSet; j++) {
                int index = i * itemsPerSet + j;
                if (index < profits.size() && index < weights.size()) {
                    items[j] = new Item(index, weights.get(index), profits.get(index));
                }
            }
            
            ItemSet itemSet = new ItemSet(i, items);
            problem.addItemSet(itemSet);
        }
    }
    
    public static void main(String[] args) {
        try {
            IdkpDataFileReader reader = new IdkpDataFileReader();
            String filePath = "Four kinds of D{0-1}KP instances/idkp1-10.txt";
            List<KnapsackProblem> problems = reader.readIdkpFile(filePath);
            
            System.out.println("成功读取 " + problems.size() + " 个实例");
            for (int i = 0; i < problems.size(); i++) {
                KnapsackProblem problem = problems.get(i);
                System.out.println("实例 " + (i + 1) + ":");
                System.out.println("  容量: " + problem.getCapacity());
                System.out.println("  项集数量: " + problem.getItemCount());
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
