package ru.kir.wiley.cache;

import org.junit.After;
import org.junit.Test;
import ru.kir.wiley.cache.impl.SimpleCache;
import ru.kir.wiley.cache.strategies.eviction.EvictionStrategy;

import static org.junit.Assert.*;
import static ru.kir.wiley.cache.strategies.eviction.EvictionStrategy.EvictionStrategyType.LRU;

/**
 * Created by Kirill Zhitelev on 27.10.2019.
 */
public class CacheLRUStrategyTest extends CacheTest {
    private Cache<Integer, Integer> cache = new SimpleCache<>(3, LRU);

    @After
    public void clear() {
        cache.clear();
    }

    @Test
    public void successPutAndGetAndRemoveSingleElement() {
        cache.putObject(key0, value0);
        assertFalse(cache.containsKey(key1));
        assertEquals(22, cache.getObject(key0).intValue());
        assertEquals(1, cache.size());

        cache.removeObject(key0);
        assertNull(cache.getObject(key0));
        assertEquals(0, cache.size());
    }

    @Test
    public void successFillCache() {
        cache.putObject(key0, value0);
        cache.putObject(key1, value1);
        cache.putObject(key2, value2);
        assertEquals(3, cache.size());

        assertTrue(cache.containsValue(new EvictionStrategy.Node<>(key0, value0)));
        assertTrue(cache.containsValue(new EvictionStrategy.Node<>(key1, value1)));
        assertTrue(cache.containsValue(new EvictionStrategy.Node<>(key2, value2)));
    }

    @Test
    public void successCacheClear() {
        cache.putObject(key0, value0);
        cache.putObject(key1, value1);
        cache.putObject(key2, value2);
        assertEquals(3, cache.size());

        cache.clear();
        assertEquals(0, cache.size());
    }

    @Test
    public void evictLeastRecentlyUsedElement() {
        cache.putObject(key0, value0);
        cache.putObject(key1, value2);
        cache.putObject(key2, value3);

        cache.getObject(key0);
        cache.getObject(key0);
        cache.getObject(key2);

        assertEquals(3, cache.size());

        cache.putObject(key3, value2);

        assertEquals(3, cache.size());

        assertNull(cache.getObject(key1));
        assertTrue(cache.containsValue(new EvictionStrategy.Node<>(key0, value0)));
        assertTrue(cache.containsValue(new EvictionStrategy.Node<>(key2, value3)));
        assertTrue(cache.containsValue(new EvictionStrategy.Node<>(key3, value2)));
    }

    @Test
    public void evictFirstAddedElement() {
        cache.putObject(key0, value0);
        cache.putObject(key1, value2);
        cache.putObject(key2, value3);
        cache.putObject(key3, value2);

        assertEquals(3, cache.size());

        assertNull(cache.getObject(key0));
        assertTrue(cache.containsValue(new EvictionStrategy.Node<>(key1, value2)));
        assertTrue(cache.containsValue(new EvictionStrategy.Node<>(key2, value3)));
        assertTrue(cache.containsValue(new EvictionStrategy.Node<>(key3, value2)));
    }

    @Test
    public void successPutElementWithSameKey() {
        cache.putObject(key0, value0);
        cache.putObject(key0, value2);
        cache.putObject(key0, value3);
        cache.putObject(key3, value2);

        assertEquals(2, cache.size());
        assertTrue(cache.containsValue(new EvictionStrategy.Node<>(key0, value3)));
    }

    @Test
    public void evictLeastRecentlyUsedElementNotAsLFU() {
        cache.putObject(key0, value0);
        cache.putObject(key1, value2);

        cache.getObject(key0);
        cache.getObject(key1);
        cache.getObject(key0);

        cache.putObject(key2, value3);
        cache.putObject(key3, value3);

        assertEquals(3, cache.size());

        assertNull(cache.getObject(key1));
        assertTrue(cache.containsValue(new EvictionStrategy.Node<>(key0, value0)));
        assertTrue(cache.containsValue(new EvictionStrategy.Node<>(key2, value3)));
        assertTrue(cache.containsValue(new EvictionStrategy.Node<>(key3, value3)));
    }
}
