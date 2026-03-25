package com.knapsack.io;

import com.knapsack.model.Item;
import com.knapsack.model.ItemSet;
import com.knapsack.model.KnapsackProblem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataFileReader {
    private static final int ITEMS_PER_SET = 3;

    public KnapsackProblem readFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            double capacity = 0;
            List<ItemSet> itemSets = new ArrayList<>();
            int itemSetId = 0;
            Item[] currentItems = new Item[ITEMS_PER_SET];
            int itemIndex = 0;
            int itemId = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                if (lineNumber == 1) {
                    try {
                        capacity = Double.parseDouble(line);
                    } catch (NumberFormatException e) {
                        throw new IOException("Invalid capacity format at line " + lineNumber);
                    }
                } else {
                    String[] parts = line.split("\\s+");
                    if (parts.length >= 2) {
                        try {
                            double weight = Double.parseDouble(parts[0]);
                            double value = Double.parseDouble(parts[1]);
                            currentItems[itemIndex] = new Item(itemId++, weight, value);
                            itemIndex++;

                            if (itemIndex == ITEMS_PER_SET) {
                                itemSets.add(new ItemSet(itemSetId++, currentItems));
                                currentItems = new Item[ITEMS_PER_SET];
                                itemIndex = 0;
                            }
                        } catch (NumberFormatException e) {
                            throw new IOException("Invalid item format at line " + lineNumber);
                        }
                    }
                }
            }

            KnapsackProblem problem = new KnapsackProblem(capacity);
            for (ItemSet itemSet : itemSets) {
                problem.addItemSet(itemSet);
            }

            return problem;
        }
    }

    public List<String> readFileLines(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}
