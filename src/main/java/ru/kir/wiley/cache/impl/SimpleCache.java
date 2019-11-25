package ru.kir.wiley.cache.impl;

import ru.kir.wiley.cache.Cache;
import ru.kir.wiley.cache.strategies.eviction.EvictionStrategy;
import ru.kir.wiley.cache.strategies.eviction.EvictionStrategyFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kirill Zhitelev on 27.10.2019.
 */
public class SimpleCache<K, V> implements Cache<K, V> {
    private Map<K, EvictionStrategy.Node<K, V>> cache;
    private int capacity;
    private EvictionStrategy<K, V> strategy;

    public SimpleCache(int capacity, EvictionStrategy.EvictionStrategyType type) {
        this.capacity = capacity;
        this.cache = new HashMap<>(this.capacity);
        this.strategy = EvictionStrategyFactory.getEvictionStrategy(type, cache, capacity);
    }

    @Override
    public void putObject(K key, V value) {
        strategy.putObserver(new EvictionStrategy.Node<>(key, value));
        cache.put(key, new EvictionStrategy.Node<>(key, value));
    }

    @Override
    public V getObject(K key) {
        EvictionStrategy.Node<K, V> value = cache.get(key);
        if (value != null) {
            strategy.getObserver(value);
            return value.getValue();
        }
        return null;
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public void removeObject(K key) {
        strategy.removeObserver(cache.get(key));
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public boolean containsValue(EvictionStrategy.Node<K, V> value) {
        return cache.containsValue(value);
    }

    @Override
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    @Override
    public String toString() {
        return cache.toString();
    }


}
