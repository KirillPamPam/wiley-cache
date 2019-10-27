package ru.kir.wiley.cache.strategies.eviction;

import java.util.Map;

/**
 * Created by Kirill Zhitelev on 27.10.2019.
 */
public final class EvictionStrategyFactory {
    public static <K, V> EvictionStrategy getEvictionStrategy(EvictionStrategy.EvictionStrategyType type,
                                                              Map<K, EvictionStrategy.Node<K, V>> cache, int capacity) {
        switch (type) {
            case LRU:
                return new LRUStrategy<>(cache, capacity);
            case LFU:
                return new LFUStrategy<>(cache, capacity);
            default:
                throw new IllegalArgumentException("There is no strategy type: " + type);
        }
    }
}
