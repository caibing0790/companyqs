package com.flexport;

import com.flexport.warehouseinventory.WarehouseInventory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseInventoryTest {

    @Test
    public void testGetInventoryAtTime() {
        List<int[]> loads = Arrays.asList(
                new int[]{1, 5, 10},
                new int[]{3, 8, 5},
                new int[]{6, 10, 7}
        );

        WarehouseInventory warehouseInventory = new WarehouseInventory(loads);

        // 测试时间点 2
        assertEquals(10, warehouseInventory.getInventoryAtTime(2)); // 只有第一个load在库

        // 测试时间点 4
        assertEquals(15, warehouseInventory.getInventoryAtTime(4)); // 第一个和第二个load在库

        // 测试时间点 7
        assertEquals(12, warehouseInventory.getInventoryAtTime(7)); // 第二个和第三个load在库

        // 测试时间点 10
        assertEquals(0, warehouseInventory.getInventoryAtTime(10)); // 没有load在库
    }

    @Test
    public void testGetMaxInventoryPeriod() {
        List<int[]> loads = Arrays.asList(
                new int[]{1, 5, 10},
                new int[]{3, 8, 5},
                new int[]{6, 10, 7}
        );

        WarehouseInventory warehouseInventory = new WarehouseInventory(loads);
        // 测试最大库存量及其时间段
        assertArrayEquals(new int[]{3, 5, 15}, warehouseInventory.getMaxInventoryPeriod()); // 在时间区间 [3, 5] 内最大库存为 15
    }

    @Test
    public void testMultipleMaxPeriods() {
        List<int[]> loads = Arrays.asList(
                new int[]{1, 4, 10},
                new int[]{3, 6, 10},
                new int[]{5, 7, 10}
        );

        WarehouseInventory warehouseInventory = new WarehouseInventory(loads);

        // 测试多个最大库存量区间
        int[] result = warehouseInventory.getMaxInventoryPeriod();
        boolean validResult = (result[0] == 3 && result[1] == 4 && result[2] == 20) ||
                (result[0] == 5 && result[1] == 6 && result[2] == 20);
        assertTrue(validResult);
    }
}