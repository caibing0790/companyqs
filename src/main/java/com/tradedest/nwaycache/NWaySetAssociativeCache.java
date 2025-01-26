package com.tradedest.nwaycache;

import java.util.List;

public class NWaySetAssociativeCache<K, V, M> implements Cache<K, V, M> {

    public NWaySetAssociativeCache(int setSize, int entrySize, ReplacementAlgorithm<K, V, M> replacementAlgorithm) {
    }

    @Override
    public void put(K key, V value) {

    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public List<CacheElement<K, V, M>>[] getCacheContents() {
        return null;
    }
}
