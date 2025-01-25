package com.tradedest.nwaycache;

public interface Cache<K, V, M> {
    void put(K key, V value);

    V get(K key);
}
