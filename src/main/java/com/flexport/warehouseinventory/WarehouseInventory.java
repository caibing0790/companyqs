package com.flexport.warehouseinventory;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WarehouseInventory {
    private final List<int[]> inventories;

    public WarehouseInventory(List<int[]> inventories) {
        this.inventories = inventories;
    }

    public int getInventoryAtTime(int time) {
        if (time < 0) {
            throw new IllegalArgumentException("Time cannot be negative");
        }
        int totalInventory = 0;
        for (int[] inventory : inventories) {
            if (time >= inventory[0] && time < inventory[1]) {
                totalInventory += inventory[2];
            }
        }
        return totalInventory;
    }

    public int[] getMaxInventoryPeriod() {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();

        for (int[] inventory : inventories) {
            int startTime = inventory[0];
            int endTime = inventory[1];
            int inventoryAmount = inventory[2];
            treeMap.put(startTime, treeMap.getOrDefault(startTime, 0) + inventoryAmount);
            treeMap.put(endTime, treeMap.getOrDefault(endTime, 0) - inventoryAmount);
        }

        int maxInventory = 0;
        int maxStartTime = 0;
        int maxEndTime = 0;
        int currentInventory = 0;
        boolean isMaxInventoryPeriod = false;

        for (Map.Entry<Integer, Integer> entry : treeMap.entrySet()) {
            currentInventory = currentInventory + entry.getValue();

            if (currentInventory > maxInventory) {
                maxInventory = currentInventory;
                maxStartTime = entry.getKey();
                isMaxInventoryPeriod = true;
            } else if (isMaxInventoryPeriod && currentInventory < maxInventory) {
                maxEndTime = entry.getKey();
                isMaxInventoryPeriod = false;
            }
        }
        if (isMaxInventoryPeriod) {
            maxEndTime = treeMap.lastKey();
        }

        return new int[]{maxStartTime, maxEndTime, maxInventory};
    }
}
