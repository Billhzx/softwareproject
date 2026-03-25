package com.knapsack.util;

import com.knapsack.model.ItemSet;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChartPanel extends JPanel {
    private List<ItemSet> itemSets;
    private String title;
    private static final int PADDING = 50;
    private static final int POINT_RADIUS = 5;

    public ChartPanel(List<ItemSet> itemSets, String title) {
        this.itemSets = itemSets;
        this.title = title;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(700, 500));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("宋体", Font.BOLD, 16));
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2d.drawString(title, (width - titleWidth) / 2, 25);

        if (itemSets == null || itemSets.isEmpty()) {
            g2d.setFont(new Font("宋体", Font.PLAIN, 14));
            g2d.drawString("无数据", width / 2 - 20, height / 2);
            return;
        }

        double maxWeight = 0;
        double maxValue = 0;

        for (ItemSet itemSet : itemSets) {
            for (int i = 0; i < 3; i++) {
                double weight = itemSet.getItem(i).getWeight();
                double value = itemSet.getItem(i).getValue();
                if (weight > maxWeight) maxWeight = weight;
                if (value > maxValue) maxValue = value;
            }
        }

        maxWeight *= 1.1;
        maxValue *= 1.1;

        int legendWidth = 100;
        int chartWidth = width - 2 * PADDING - legendWidth;
        int chartHeight = height - 2 * PADDING;
        int originX = PADDING;
        int originY = height - PADDING;

        g2d.setColor(Color.GRAY);
        g2d.drawLine(originX, originY, originX + chartWidth, originY);
        g2d.drawLine(originX, originY, originX, originY - chartHeight);

        g2d.setFont(new Font("宋体", Font.PLAIN, 12));
        g2d.drawString("重量", originX + chartWidth / 2 - 15, originY + 35);

        g2d.rotate(-Math.PI / 2, originX - 35, originY - chartHeight / 2);
        g2d.drawString("价值", originX - 35, originY - chartHeight / 2);
        g2d.rotate(Math.PI / 2, originX - 35, originY - chartHeight / 2);

        for (int i = 0; i <= 5; i++) {
            double xValue = maxWeight * i / 5;
            int x = originX + (int) (chartWidth * i / 5);
            g2d.drawString(String.format("%.1f", xValue), x - 15, originY + 15);

            double yValue = maxValue * i / 5;
            int y = originY - (int) (chartHeight * i / 5);
            g2d.drawString(String.format("%.1f", yValue), originX - 40, y + 5);
        }

        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN};
        String[] labels = {"物品1", "物品2", "物品3"};

        for (int k = 0; k < 3; k++) {
            g2d.setColor(colors[k]);
            for (ItemSet itemSet : itemSets) {
                double weight = itemSet.getItem(k).getWeight();
                double value = itemSet.getItem(k).getValue();

                int x = originX + (int) (chartWidth * weight / maxWeight);
                int y = originY - (int) (chartHeight * value / maxValue);

                g2d.fillOval(x - POINT_RADIUS, y - POINT_RADIUS, 
                            POINT_RADIUS * 2, POINT_RADIUS * 2);
            }

            g2d.setColor(colors[k]);
            g2d.fillRect(originX + chartWidth + 20, originY - chartHeight + k * 30, 15, 15);
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("宋体", Font.PLAIN, 12));
            g2d.drawString(labels[k], originX + chartWidth + 40, originY - chartHeight + k * 30 + 12);
        }
    }

    public void displayInFrame() {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
