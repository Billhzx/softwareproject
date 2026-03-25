package com.knapsack.ui;

import com.knapsack.algorithm.DynamicProgrammingSolver;
import com.knapsack.io.DataFileReader;
import com.knapsack.io.ResultExporter;
import com.knapsack.model.ItemSet;
import com.knapsack.model.KnapsackProblem;
import com.knapsack.model.SolutionResult;
import com.knapsack.util.ChartPanel;
import com.knapsack.util.DataSorter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class MainFrame extends JFrame {
    private KnapsackProblem currentProblem;
    private SolutionResult currentResult;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private JTextArea resultArea;
    private JLabel capacityLabel;
    private JLabel itemCountLabel;

    public MainFrame() {
        setTitle("D{0-1}背包问题求解器");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("文件");
        JMenuItem openItem = new JMenuItem("打开数据文件");
        JMenuItem exitItem = new JMenuItem("退出");

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDataFile();
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        fileMenu.add(openItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        infoPanel.setBorder(new TitledBorder("问题信息"));
        capacityLabel = new JLabel("背包容量: 未加载");
        itemCountLabel = new JLabel("项集数量: 未加载");
        infoPanel.add(capacityLabel);
        infoPanel.add(itemCountLabel);
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        JPanel dataPanel = new JPanel(new BorderLayout());
        dataPanel.setBorder(new TitledBorder("数据列表"));
        String[] columnNames = {"项集ID", "物品1(重量,价值)", "物品2(重量,价值)", "物品3(重量,价值)", "价值/重量比"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dataTable = new JTable(tableModel);
        dataTable.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        dataPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton sortButton = new JButton("按价值/重量比排序");
        JButton chartButton = new JButton("显示散点图");

        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortData();
            }
        });

        chartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showChart();
            }
        });

        buttonPanel.add(sortButton);
        buttonPanel.add(chartButton);
        dataPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(new TitledBorder("求解结果"));
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultPanel.add(resultScrollPane, BorderLayout.CENTER);

        JPanel solveButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton solveButton = new JButton("求解最优解");
        JButton exportTxtButton = new JButton("导出TXT");
        JButton exportCsvButton = new JButton("导出CSV");

        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveProblem();
            }
        });

        exportTxtButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportResult("txt");
            }
        });

        exportCsvButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportResult("csv");
            }
        });

        solveButtonPanel.add(solveButton);
        solveButtonPanel.add(exportTxtButton);
        solveButtonPanel.add(exportCsvButton);
        resultPanel.add(solveButtonPanel, BorderLayout.SOUTH);

        centerPanel.add(dataPanel);
        centerPanel.add(resultPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void openDataFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Text Files (*.txt)", "txt"));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                DataFileReader reader = new DataFileReader();
                currentProblem = reader.readFile(file.getAbsolutePath());
                updateDataDisplay();
                JOptionPane.showMessageDialog(this, "数据文件加载成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "加载数据文件失败: " + ex.getMessage(), 
                        "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateDataDisplay() {
        if (currentProblem == null) {
            return;
        }

        capacityLabel.setText(String.format("背包容量: %.2f", currentProblem.getCapacity()));
        itemCountLabel.setText("项集数量: " + currentProblem.getItemCount());

        tableModel.setRowCount(0);
        List<ItemSet> itemSets = currentProblem.getItemSets();

        for (ItemSet itemSet : itemSets) {
            Object[] row = {
                itemSet.getId(),
                String.format("(%.2f, %.2f)", itemSet.getItem(0).getWeight(), itemSet.getItem(0).getValue()),
                String.format("(%.2f, %.2f)", itemSet.getItem(1).getWeight(), itemSet.getItem(1).getValue()),
                String.format("(%.2f, %.2f)", itemSet.getItem(2).getWeight(), itemSet.getItem(2).getValue()),
                String.format("%.4f", itemSet.getThirdItemValueToWeightRatio())
            };
            tableModel.addRow(row);
        }

        resultArea.setText("");
        currentResult = null;
    }

    private void sortData() {
        if (currentProblem == null) {
            JOptionPane.showMessageDialog(this, "请先加载数据文件！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DataSorter.sortByThirdItemValueToWeightRatio(currentProblem.getItemSets());
        updateDataDisplay();
        JOptionPane.showMessageDialog(this, "数据已按价值/重量比排序！", "成功", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showChart() {
        if (currentProblem == null) {
            JOptionPane.showMessageDialog(this, "请先加载数据文件！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ChartPanel chartPanel = new ChartPanel(currentProblem.getItemSets(), "D{0-1}背包问题数据散点图");
        chartPanel.displayInFrame();
    }

    private void solveProblem() {
        if (currentProblem == null) {
            JOptionPane.showMessageDialog(this, "请先加载数据文件！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            DynamicProgrammingSolver solver = new DynamicProgrammingSolver();
            currentResult = solver.solve(currentProblem);
            displayResult();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "求解失败: " + ex.getMessage(), 
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayResult() {
        if (currentResult == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("求解结果\n");
        sb.append("========================================\n");
        sb.append(String.format("算法: %s\n", currentResult.getAlgorithm()));
        sb.append(String.format("求解时间: %d ms\n", currentResult.getSolveTime()));
        sb.append(String.format("总价值: %.2f\n", currentResult.getTotalValue()));
        sb.append(String.format("总重量: %.2f\n", currentResult.getTotalWeight()));
        sb.append("========================================\n");
        sb.append("选中物品详情:\n");
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-10s %-10s %-10s %-10s\n", "项集ID", "物品索引", "重量", "价值"));
        sb.append("----------------------------------------\n");

        for (var item : currentResult.getSelectedItems()) {
            sb.append(String.format("%-10d %-10d %-10.2f %-10.2f\n",
                    item.getItemSetId(), item.getItemIndex(),
                    item.getWeight(), item.getValue()));
        }

        sb.append("========================================\n");
        sb.append(String.format("共选中 %d 个物品\n", currentResult.getSelectedItems().size()));
        sb.append("========================================\n");

        resultArea.setText(sb.toString());
    }

    private void exportResult(String format) {
        if (currentResult == null) {
            JOptionPane.showMessageDialog(this, "请先求解问题！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 使用用户目录作为保存位置
            String userHome = System.getProperty("user.home");
            String saveDirPath = userHome + File.separator + "Documents" + File.separator + "KnapsackResults";
            File saveDir = new File(saveDirPath);
            
            // 确保保存目录存在
            if (!saveDir.exists()) {
                if (!saveDir.mkdirs()) {
                    // 如果Documents目录也有问题，尝试使用用户主目录
                    saveDirPath = userHome + File.separator + "KnapsackResults";
                    saveDir = new File(saveDirPath);
                    if (!saveDir.exists() && !saveDir.mkdirs()) {
                        // 最后尝试使用临时目录
                        saveDirPath = System.getProperty("java.io.tmpdir");
                        saveDir = new File(saveDirPath);
                    }
                }
            }

            // 生成默认文件名
            String fileName = "knapsack_result_" + System.currentTimeMillis() + "." + format;
            File defaultFile = new File(saveDir, fileName);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(saveDir);
            fileChooser.setSelectedFile(defaultFile);
            
            int result = fileChooser.showSaveDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                
                try {
                    // 确保目录存在
                    File parentDir = file.getParentFile();
                    if (parentDir != null && !parentDir.exists()) {
                        if (!parentDir.mkdirs()) {
                            JOptionPane.showMessageDialog(this, "无法创建目录: " + parentDir.getAbsolutePath(), 
                                    "错误", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    
                    // 尝试使用不同的文件写入方式
                    boolean success = false;
                    try {
                        ResultExporter exporter = new ResultExporter();
                        if (format.equals("txt")) {
                            exporter.exportToTxt(file.getAbsolutePath(), currentResult);
                        } else if (format.equals("csv")) {
                            exporter.exportToCsv(file.getAbsolutePath(), currentResult);
                        }
                        success = true;
                    } catch (Exception e) {
                        // 如果失败，尝试使用绝对路径
                        String absolutePath = file.getAbsolutePath();
                        ResultExporter exporter = new ResultExporter();
                        if (format.equals("txt")) {
                            exporter.exportToTxt(absolutePath, currentResult);
                        } else if (format.equals("csv")) {
                            exporter.exportToCsv(absolutePath, currentResult);
                        }
                        success = true;
                    }
                    
                    if (success) {
                        JOptionPane.showMessageDialog(this, "结果导出成功！\n保存位置: " + file.getAbsolutePath(), 
                                "成功", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SecurityException ex) {
                    JOptionPane.showMessageDialog(this, "权限不足，无法写入文件: " + ex.getMessage() + "\n请尝试以管理员身份运行程序", 
                            "错误", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "导出失败: " + ex.getMessage() + "\n错误详情: " + ex.getStackTrace()[0], 
                            "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "导出初始化失败: " + ex.getMessage(), 
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new MainFrame().setVisible(true);
            }
        });
    }
}
