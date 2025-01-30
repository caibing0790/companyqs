import com.flexport.MinSubarraySum;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MinSubarraySumTest {

    @Test
    void testMinSubarraySum() {
        MinSubarraySum solution = new MinSubarraySum();

        // Test case 1
        int[] nums1 = {1, 5, 3, 4};
        int k1 = 2;
        int expected1 = 9; // 分割方案为 [1, 5] [3, 4]
        assertEquals(expected1, solution.minSubarraySum(nums1, k1), "Test Case 1 Failed");

        // Test case 2
        int[] nums2 = {1, 5, 3, 4};
        int k2 = 3;
        int expected2 = 6; // 分割方案为 [1] [5, 3, 4]
        assertEquals(expected2, solution.minSubarraySum(nums2, k2), "Test Case 2 Failed");

        // Test case 3: All elements in one subarray
        int[] nums3 = {1, 2, 3, 4, 5};
        int k3 = 5;
        int expected3 = 5; // 分割方案为 [1, 2, 3, 4, 5]
        assertEquals(expected3, solution.minSubarraySum(nums3, k3), "Test Case 3 Failed");

        // Test case 4: Single element arrays
        int[] nums4 = {7};
        int k4 = 1;
        int expected4 = 7; // 分割方案为 [7]
        assertEquals(expected4, solution.minSubarraySum(nums4, k4), "Test Case 4 Failed");

        // Test case 5: Multiple small segments
        int[] nums5 = {1, 2, 3, 4, 5, 6, 7, 8};
        int k5 = 2;
        int expected5 = 20; // 分割方案为 [1, 2] [3, 4] [5, 6] [7, 8]
        assertEquals(expected5, solution.minSubarraySum(nums5, k5), "Test Case 5 Failed");
    }
}