package com.tradedest.nwaycache;

import java.util.List;

public interface Cache<K, V, M> {
    void put(K key, V value);

    V get(K key);

    List<CacheElement<K, V, M>>[] getCacheContents();
}
