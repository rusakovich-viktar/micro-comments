package by.clevertec.commentsproject.cache.impl;

import by.clevertec.commentsproject.cache.Cache;
import java.util.LinkedHashMap;
import java.util.Map;


public class LruCache<K, V> extends LinkedHashMap<K, V> implements Cache<K, V> {

    private final int capacity;

    public LruCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

    @Override
    public V put(K key, V value) {
        return super.put(key, value);
    }

    @Override
    public V get(Object key) {
        return super.get(key);
    }

    @Override
    public V remove(Object key) {
        return super.remove(key);
    }
}
