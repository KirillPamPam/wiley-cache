package ru.kir.wiley.cache;

import ru.kir.wiley.cache.strategies.eviction.EvictionStrategy;

/**
 * Created by Kirill Zhitelev on 27.10.2019.
 */
public interface Cache<K, V> {
    void putObject(K key, V value);

    V getObject(K key);

    int size();

    void removeObject(K key);

    void clear();

    boolean containsValue(EvictionStrategy.Node<K, V> value);

    boolean containsKey(K key);

    void test();
}
