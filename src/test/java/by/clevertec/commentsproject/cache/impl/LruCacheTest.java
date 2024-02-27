package by.clevertec.commentsproject.cache.impl;

import static by.clevertec.commentsproject.util.TestConstant.Attributes.ONE;
import static by.clevertec.commentsproject.util.TestConstant.Attributes.THREE;
import static by.clevertec.commentsproject.util.TestConstant.Attributes.TWO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LruCacheTest {

    private LruCache<String, Integer> cache;

    @BeforeEach
    void setUp() {
        cache = new LruCache<>(2);
    }

    @Test
    void testPutAndGet() {
        cache.put(ONE, 1);
        assertEquals(1, cache.get(ONE));
        assertNull(cache.get(TWO));
    }

    @Test
    void testEviction() {
        cache.put(ONE, 1);
        cache.put(TWO, 2);
        cache.put(THREE, 3);

        assertNull(cache.get(ONE));
        assertEquals(2, cache.get(TWO));
        assertEquals(3, cache.get(THREE));
    }

    @Test
    void testUpdate() {
        cache.put(ONE, 1);
        cache.put(TWO, 2);
        cache.get(ONE);
        cache.put(THREE, 3);

        assertEquals(1, cache.get(ONE));
        assertNull(cache.get(TWO));
        assertEquals(3, cache.get(THREE));
    }

    @Test
    void testRemove() {
        cache.put(ONE, 1);
        cache.remove(ONE);

        assertNull(cache.get(ONE));
    }
}