package com.tradedest.nwaycache;

public interface ReplacementAlgorithm<K, V, M> {
    void onGet(CacheElement<K, V, M> element);
}
