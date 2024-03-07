package by.clevertec.commentsproject.cache.impl;

import by.clevertec.commentsproject.cache.Cache;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Реализация кэша с алгоритмом вытеснения LFU (Least Frequently Used) - наименее часто используемый элемент.
 */
@SuppressWarnings("unchecked")
public class LfuCache<K, V> implements Cache<K, V> {

    private final Map<K, V> vals;
    private final Map<K, Integer> counts;
    private final Map<Integer, LinkedHashSet<K>> lists;
    private int min = -1;
    private final int capacity;

    public LfuCache(int capacity) {
        this.capacity = capacity;
        vals = new HashMap<>();
        counts = new HashMap<>();
        lists = new HashMap<>();
        lists.put(1, new LinkedHashSet<>());
    }

    /**
     * Добавляет элемент в кэш. Если кэш уже полон, удаляет наименее часто используемый элемент.
     *
     * @param key   ключ элемента
     * @param value значение элемента
     * @return добавленное значение
     */
    @Override
    public V put(K key, V value) {
        if (capacity <= 0) {
            return null;
        }
        if (vals.containsKey(key)) {
            vals.put(key, value);
            get(key);
            return value;
        }
        if (vals.size() >= capacity) {
            K evict = lists.get(min).iterator().next();
            lists.get(min).remove(evict);
            vals.remove(evict);
        }
        vals.put(key, value);
        counts.put(key, 1);
        min = 1;
        lists.get(1).add(key);
        return value;
    }

    /**
     * Получает элемент из кэша по ключу. Увеличивает счетчик использования элемента.
     *
     * @param key ключ элемента
     * @return значение элемента или null, если элемент не найден
     */
    @Override
    public V get(Object key) {
        if (!vals.containsKey(key)) {
            return null;
        }
        int count = counts.get(key);
        counts.put((K) key, count + 1);
        lists.get(count).remove(key);
        if (count == min && lists.get(count).size() == 0) {
            min++;
        }
        if (!lists.containsKey(count + 1)) {
            lists.put(count + 1, new LinkedHashSet<>());
        }
        lists.get(count + 1).add((K) key);
        return vals.get(key);
    }

    /**
     * Удаляет элемент из кэша по ключу.
     *
     * @param key ключ элемента
     * @return удаленное значение или null, если элемент не найден
     */
    @Override
    public V remove(Object key) {
        V value = vals.remove(key);
        Integer count = counts.remove(key);
        if (count != null) {
            lists.get(count).remove(key);
            if (lists.get(count).isEmpty()) {
                lists.remove(count);
            }
        }
        return value;
    }
}
