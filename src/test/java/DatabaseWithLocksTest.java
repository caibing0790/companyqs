import com.tradedest.database.DatabaseWithLocks;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseWithLocksTest {

    @Test
    void testLockOperations() throws InterruptedException {
        DatabaseWithLocks<String, String> db = new DatabaseWithLocks<>();

        Thread t1 = new Thread(() -> db.create("key1", "value1"));
        Thread t2 = new Thread(() -> db.read("key1"));

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        assertEquals("value1", db.read("key1"));
    }
}