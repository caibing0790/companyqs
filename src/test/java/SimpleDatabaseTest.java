import com.tradedest.database.SimpleDatabase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimpleDatabaseTest {

    @Test
    void testCrudOperations() {
        SimpleDatabase<String, String> db = new SimpleDatabase<>();

        // Create
        db.create("key1", "value1");
        assertEquals("value1", db.read("key1"));

        // Update
        db.update("key1", "newValue");
        assertEquals("newValue", db.read("key1"));

        // Delete
        db.delete("key1");
        assertNull(db.read("key1"));
    }
}