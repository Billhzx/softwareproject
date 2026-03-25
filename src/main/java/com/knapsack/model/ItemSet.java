package com.knapsack.model;

import java.util.Arrays;

public class ItemSet {
    private int id;
    private Item[] items;

    public ItemSet(int id, Item[] items) {
        if (items == null || items.length != 3) {
            throw new IllegalArgumentException("ItemSet must contain exactly 3 items");
        }
        this.id = id;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        if (items == null || items.length != 3) {
            throw new IllegalArgumentException("ItemSet must contain exactly 3 items");
        }
        this.items = items;
    }

    public Item getItem(int index) {
        if (index < 0 || index >= 3) {
            throw new IndexOutOfBoundsException("Item index must be between 0 and 2");
        }
        return items[index];
    }

    public double getThirdItemValueToWeightRatio() {
        return items[2].getValueToWeightRatio();
    }

    @Override
    public String toString() {
        return "ItemSet{id=" + id + ", items=" + Arrays.toString(items) + "}";
    }
}
