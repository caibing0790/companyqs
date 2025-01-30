import com.tradedest.database.DatabaseWithStats;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

class DatabaseWithStatsTest {

    @Test
    void testStatsOperations() {
        DatabaseWithStats<String, String> db = new DatabaseWithStats<>();

        db.create("key1", "value1");
        db.create("key2", "value2");
        db.create("key3", "value3");
        db.read("key1");
        db.read("key2");
        db.update("key1", "newValue");

        List<Map.Entry<String, Integer>> topOps = db.getTopNOperations(3);
        assertEquals("CREATE", topOps.get(0).getKey());
        assertEquals("READ", topOps.get(1).getKey());
        assertEquals("UPDATE", topOps.get(2).getKey());
        assertEquals(3, topOps.get(0).getValue());
    }
}