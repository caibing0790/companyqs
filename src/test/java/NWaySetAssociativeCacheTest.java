import com.tradedest.nwaycache.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NWaySetAssociativeCacheTest {
    @Test
    public void testTwoWritesTwoReads() {
        Cache<String, String, Long> cache = new NWaySetAssociativeCache<>(8, 2, new LruReplacementAlgorithm<>());

        cache.put("Something", "Good");
        cache.put("Always", "Bad");

        assertEquals("Good", cache.get("Something"));
        assertEquals("Bad", cache.get("Always"));
    }

    @Test
    public void testCorrectPlacementInTheSameSet() {
        Cache<Integer, String, Long> cache = new NWaySetAssociativeCache<>(8, 2, new LruReplacementAlgorithm<>());

        cache.put(16, "Good");
        cache.put(32, "Bad");

        assertEquals("Good", cache.getCacheContents()[0].get(0).getData());
        assertEquals("Bad", cache.getCacheContents()[0].get(1).getData());
    }

    @Test
    public void testUpdateBySameKey() {
        Cache<Integer, String, Long> cache = new NWaySetAssociativeCache<>(8, 2, new LruReplacementAlgorithm<>());

        cache.put(16, "Good");
        cache.put(16, "Bad");

        assertEquals("Bad", cache.get(16));
    }

    @Test
    public void testLeastRecentlyUsedAlgorithmWorks() throws InterruptedException {
        Cache<Integer, String, Long> cache = new NWaySetAssociativeCache<>(8, 2, new LruReplacementAlgorithm<>());

        cache.put(16, "Good");
        Thread.sleep(50);
        cache.put(32, "Bad");
        Thread.sleep(50);
        cache.put(48, "Ugly");

        assertNull(cache.get(16));
        assertEquals("Bad", cache.get(32));
        assertEquals("Ugly", cache.get(48));
    }

    @Test
    public void testMostRecentlyUsedAlgorithmWorks() throws InterruptedException {
        Cache<Integer, String, Long> cache = new NWaySetAssociativeCache<>(8, 2, new MruReplacementAlgorithm<>());

        cache.put(16, "Good");
        Thread.sleep(50);
        cache.put(32, "Bad");
        Thread.sleep(50);
        cache.put(48, "Ugly");
        System.out.println(cache.get(48));

        assertEquals("Good", cache.get(16));
        assertNull(cache.get(32));
        assertEquals("Ugly", cache.get(48));
    }

    @Test
    public void testOurCustomerImplementCacheThatChooseTheFirstOneToEvictWorks() throws InterruptedException {
        Cache<Integer, String, Long> cache = new NWaySetAssociativeCache<>(8, 4, new LruReplacementAlgorithm<>());

        cache.put(16, "Good");
        Thread.sleep(50);
        cache.put(32, "Bad");
        Thread.sleep(50);
        cache.put(48, "Ugly");
        Thread.sleep(50);
        cache.put(64, "Something");
        Thread.sleep(50);
        cache.put(80, "Wrong");

        assertNull(cache.get(16));
        assertEquals("Bad", cache.get(32));
        assertEquals("Ugly", cache.get(48));
        assertEquals("Something", cache.get(64));
        assertEquals("Wrong", cache.get(80));
    }

    @Test
    public void testFrequencyBasedAlgorithmWorks() {
        Cache<Integer, String, Long> cache = new NWaySetAssociativeCache<>(1, 2, new LfuReplacementAlgorithm<>());

        cache.put(16, "Good");
        cache.put(32, "Bad");
        cache.get(16);
        cache.get(32);
        cache.get(32);
        cache.put(24, "New Stuff");

        assertEquals("Bad", cache.get(32));
        assertNull(cache.get(16));
    }
}
