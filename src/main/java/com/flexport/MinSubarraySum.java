package com.flexport;

/**
 * 输入一个数组 nums，一个整数 k，将 nums 数组分割为长度最大为 k 的子数组，每个子数组取最大值并求和，找出和为最小的分割方案数。
 */
public class MinSubarraySum {
    public int minSubarraySum(int[] nums, int k) {
        int n = nums.length;
        if (n == 0) {
            return 0;
        }
        int[] dp = new int[n + 1];

        for (int i = 1; i <= nums.length; i++) {
            dp[i] = Integer.MAX_VALUE;
            int temp_max = 0;
            for (int j = 1; j <= Math.min(k, i); j++) {
                temp_max = Math.max(temp_max, nums[i - j]);
                dp[i] = Math.min(dp[i], dp[i - j] + temp_max);
            }
        }

        return dp[n];
    }
}
