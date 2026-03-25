package com.knapsack.ui;

import com.knapsack.algorithm.DynamicProgrammingSolver;
import com.knapsack.io.DataFileReader;
import com.knapsack.io.IdkpDataFileReader;
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
                // 首先尝试使用标准格式读取
                DataFileReader reader = new DataFileReader();
                currentProblem = reader.readFile(file.getAbsolutePath());
                updateDataDisplay();
                JOptionPane.showMessageDialog(this, "数据文件加载成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                // 如果标准格式读取失败，尝试IDKP格式
                try {
                    IdkpDataFileReader idkpReader = new IdkpDataFileReader();
                    java.util.List<KnapsackProblem> problems = idkpReader.readIdkpFile(file.getAbsolutePath());
                    
                    if (problems.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "文件中没有找到有效的D{0-1}KP实例", 
                                "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // 如果有多个实例，让用户选择
                    if (problems.size() > 1) {
                        String[] options = new String[problems.size()];
                        for (int i = 0; i < problems.size(); i++) {
                            options[i] = "实例 " + (i + 1) + " (容量: " + problems.get(i).getCapacity() + ", 项集: " + problems.get(i).getItemCount() + ")";
                        }
                        
                        String selected = (String) JOptionPane.showInputDialog(this, 
                                "文件中包含多个实例，请选择要加载的实例：",
                                "选择实例",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]);
                        
                        if (selected != null) {
                            int selectedIndex = 0;
                            for (int i = 0; i < options.length; i++) {
                                if (options[i].equals(selected)) {
                                    selectedIndex = i;
                                    break;
                                }
                            }
                            currentProblem = problems.get(selectedIndex);
                        } else {
                            return; // 用户取消选择
                        }
                    } else {
                        currentProblem = problems.get(0);
                    }
                    
                    updateDataDisplay();
                    JOptionPane.showMessageDialog(this, "IDKP数据文件加载成功！\n共读取 " + problems.size() + " 个实例", "成功", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex2) {
                    JOptionPane.showMessageDialog(this, "加载数据文件失败: " + ex.getMessage(), 
                            "错误", JOptionPane.ERROR_MESSAGE);
                }
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
            // 尝试多个可能的保存位置
            String[] possibleDirs = new String[]{
                System.getProperty("user.home") + File.separator + "Downloads",
                System.getProperty("user.home") + File.separator + "Desktop",
                System.getProperty("user.home"),
                System.getProperty("java.io.tmpdir")
            };
            
            File saveDir = null;
            for (String dirPath : possibleDirs) {
                File dir = new File(dirPath);
                if (dir.exists() && dir.isDirectory() && dir.canWrite()) {
                    saveDir = dir;
                    break;
                }
            }
            
            if (saveDir == null) {
                JOptionPane.showMessageDialog(this, "无法找到可写入的目录，请检查系统权限", 
                        "错误", JOptionPane.ERROR_MESSAGE);
                return;
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
                    
                    // 检查文件是否可写
                    if (file.exists() && !file.canWrite()) {
                        JOptionPane.showMessageDialog(this, "文件已存在且无法写入，请选择其他文件名", 
                                "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // 尝试使用不同的文件写入方式
                    boolean success = false;
                    try {
                        // 直接写入文件
                        StringBuilder content = new StringBuilder();
                        if (format.equals("txt")) {
                            buildTxtContent(content, currentResult);
                        } else if (format.equals("csv")) {
                            buildCsvContent(content, currentResult);
                        }
                        
                        // 使用FileOutputStream写入
                        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file)) {
                            fos.write(content.toString().getBytes("UTF-8"));
                        }
                        success = true;
                    } catch (Exception e) {
                        // 如果失败，尝试使用RandomAccessFile
                        try (java.io.RandomAccessFile raf = new java.io.RandomAccessFile(file, "rw")) {
                            StringBuilder content = new StringBuilder();
                            if (format.equals("txt")) {
                                buildTxtContent(content, currentResult);
                            } else if (format.equals("csv")) {
                                buildCsvContent(content, currentResult);
                            }
                            raf.writeBytes(content.toString());
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
                    // 显示详细错误信息
                    StringBuilder errorMsg = new StringBuilder();
                    errorMsg.append("导出失败: " + ex.getMessage() + "\n");
                    errorMsg.append("文件路径: " + file.getAbsolutePath() + "\n");
                    errorMsg.append("目录是否存在: " + file.getParentFile().exists() + "\n");
                    errorMsg.append("目录是否可写: " + file.getParentFile().canWrite() + "\n");
                    errorMsg.append("文件是否存在: " + file.exists() + "\n");
                    errorMsg.append("错误详情: " + ex.getStackTrace()[0] + "\n");
                    
                    // 显示备选方案
                    int option = JOptionPane.showConfirmDialog(this, 
                            errorMsg.toString() + "\n是否显示结果内容以便手动复制？", 
                            "错误", JOptionPane.YES_NO_OPTION);
                    
                    if (option == JOptionPane.YES_OPTION) {
                        showResultContent(format, currentResult);
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "导出初始化失败: " + ex.getMessage(), 
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buildTxtContent(StringBuilder content, SolutionResult result) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        content.append("========================================\n");
        content.append("D{0-1}背包问题求解结果\n");
        content.append("========================================\n");
        content.append("导出时间: " + sdf.format(new java.util.Date()) + "\n");
        content.append("算法: " + result.getAlgorithm() + "\n");
        content.append("求解时间: " + result.getSolveTime() + " ms\n");
        content.append("总价值: " + String.format("%.2f", result.getTotalValue()) + "\n");
        content.append("总重量: " + String.format("%.2f", result.getTotalWeight()) + "\n");
        content.append("========================================\n");
        content.append("选中物品详情:\n");
        content.append("----------------------------------------\n");
        content.append(String.format("%-10s %-10s %-10s %-10s\n", "项集ID", "物品索引", "重量", "价值"));
        content.append("----------------------------------------\n");

        for (var item : result.getSelectedItems()) {
            content.append(String.format("%-10d %-10d %-10.2f %-10.2f\n",
                    item.getItemSetId(), item.getItemIndex(),
                    item.getWeight(), item.getValue()));
        }

        content.append("========================================\n");
        content.append("共选中 " + result.getSelectedItems().size() + " 个物品\n");
        content.append("========================================\n");
    }
    
    private void buildCsvContent(StringBuilder content, SolutionResult result) {
        // 添加UTF-8 BOM标记，帮助Excel正确识别编码
        content.append("\ufeff");
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        content.append("导出时间," + sdf.format(new java.util.Date()) + "\n");
        content.append("算法," + result.getAlgorithm() + "\n");
        content.append("求解时间(ms)," + result.getSolveTime() + "\n");
        content.append("总价值," + String.format("%.2f", result.getTotalValue()) + "\n");
        content.append("总重量," + String.format("%.2f", result.getTotalWeight()) + "\n");
        content.append("\n");
        content.append("项集ID,物品索引,重量,价值\n");

        for (var item : result.getSelectedItems()) {
            content.append(String.format("%d,%d,%.2f,%.2f\n",
                    item.getItemSetId(), item.getItemIndex(),
                    item.getWeight(), item.getValue()));
        }
    }
    
    private void showResultContent(String format, SolutionResult result) {
        StringBuilder content = new StringBuilder();
        if (format.equals("txt")) {
            buildTxtContent(content, result);
        } else if (format.equals("csv")) {
            buildCsvContent(content, result);
        }
        
        JTextArea textArea = new JTextArea(content.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "求解结果（请复制）", JOptionPane.INFORMATION_MESSAGE);
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
