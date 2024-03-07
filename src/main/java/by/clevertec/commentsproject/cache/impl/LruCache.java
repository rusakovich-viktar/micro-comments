package by.clevertec.commentsproject.cache.impl;

import by.clevertec.commentsproject.cache.Cache;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Реализация кэша с алгоритмом вытеснения LRU (Least Recently Used) - удаляет из памяти те объекты, к которым дольше всего не было обращений.
 */
public class LruCache<K, V> extends LinkedHashMap<K, V> implements Cache<K, V> {

    private final int capacity;

    public LruCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    /**
     * Определяет, следует ли удалить самый старый элемент. Элемент удаляется, если размер кэша превышает его емкость.
     *
     * @param eldest самый старый элемент
     * @return true, если следует удалить самый старый элемент, иначе false
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

    /**
     * Добавляет элемент в кэш.
     *
     * @param key   ключ элемента
     * @param value значение элемента
     * @return добавленное значение
     */
    @Override
    public V put(K key, V value) {
        return super.put(key, value);
    }

    /**
     * Получает элемент из кэша по ключу.
     *
     * @param key ключ элемента
     * @return значение элемента или null, если элемент не найден
     */
    @Override
    public V get(Object key) {
        return super.get(key);
    }

    /**
     * Удаляет элемент из кэша по ключу.
     *
     * @param key ключ элемента
     * @return удаленное значение или null, если элемент не найден
     */
    @Override
    public V remove(Object key) {
        return super.remove(key);
    }
}
