package com.tradedest.nwaycache;

public interface ReplacementAlgorithm<K, V, M> {
    void onGet(CacheElement<K, V, M> element);
    void onSet(CacheElement<K, V, M> element);
    int compare(CacheElement<K, V, M> element1, CacheElement<K, V, M> element2);
}
