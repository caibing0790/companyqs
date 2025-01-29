package com.tradedest.nwaycache;

import java.util.Objects;

public class CacheElement<K, V, M> {
    private K key;
    private V data;
    private M metadata;

    public CacheElement(K key, V data) {
        this.key = key;
        this.data = data;
    }

    public V getData() {
        return data;
    }

    public K getKey() {
        return key;
    }

    public void setData(V data) {
        this.data = data;
    }

    public void setKeyData(K key, V data) {
        this.key = key;
        this.data = data;
    }

    public void setMetadata(M metadata) {
        this.metadata = metadata;
    }

    public M getMetadata() {
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CacheElement<?, ?, ?> that = (CacheElement<?, ?, ?>) o;
        return Objects.equals(key, that.key) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, data);
    }
}
