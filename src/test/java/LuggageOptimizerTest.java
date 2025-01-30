import com.flexport.LuggageOptimizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LuggageOptimizerTest {
    @Test
    public void testMinCostToBuyAtDestination() {
        int maxWeight = 20;
        int[][] things = {{3, 20}, {4, 10}};

        int expected = 10;
        int actual = LuggageOptimizer.minCostToBuyAtDestination(maxWeight, things);

        assertEquals(expected, actual);
    }
}
