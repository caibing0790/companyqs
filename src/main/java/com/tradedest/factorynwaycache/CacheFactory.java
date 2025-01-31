package com.tradedest.factorynwaycache;

public class CacheFactory {
    public static <K, V> Cache<K, V> createCache(String cacheType, int cacheSize) {
        switch (cacheType.toLowerCase()) {
            case "lru":
                return new LruCache<>(cacheSize);
            case "lfu":
                return new LfuCache<>(cacheSize);
            case "mru":
                return new MruCache<>(cacheSize);
            default:
                return null;
        }
    }
}
