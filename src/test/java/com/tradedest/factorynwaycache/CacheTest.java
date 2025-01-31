package com.tradedest.factorynwaycache;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CacheTest {

    @Test
    void testLruCache() {
        Cache<Integer, String> cache = CacheFactory.createCache("lru", 2);
        cache.put(1, "A");
        cache.put(2, "B");

        assertEquals("A", cache.get(1));
        cache.put(3, "C"); // This should evict key 2

        assertNull(cache.get(2));
        assertEquals("C", cache.get(3));
    }

    @Test
    void testLfuCache() {
        Cache<Integer, String> cache = CacheFactory.createCache("lfu", 2);
        cache.put(1, "A");
        cache.put(2, "B");

        assertEquals("A", cache.get(1));
        cache.put(3, "C"); // This should evict key 2 since it has lower frequency

        assertNull(cache.get(2));
        assertEquals("C", cache.get(3));
    }

    @Test
    void testMruCache() {
        Cache<Integer, String> cache = CacheFactory.createCache("mru", 2);
        cache.put(1, "A");
        cache.put(2, "B");

        assertEquals("A", cache.get(1));
        cache.put(3, "C"); // This should not evict key 2 because it's not the most recently used

        assertNotNull(cache.get(2));
        assertNull(cache.get(1));
    }
}