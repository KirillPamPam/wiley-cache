package ru.kir.wiley.cache.strategies.eviction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Kirill Zhitelev on 27.10.2019.
 */
public final class LFUStrategy<K, V> implements EvictionStrategy<K, V> {
    private Map<K, Node<K, V>> cache;
    private Map<Integer, LinkedList<Node<K, V>>> frequencyList = new HashMap<>();
    private Map<K, Integer> counters = new HashMap<>();
    private int cacheCapacity;
    private int min = 0;

    public LFUStrategy(Map<K, Node<K, V>> cache, int cacheCapacity) {
        this.cache = cache;
        this.cacheCapacity = cacheCapacity;
        frequencyList.put(1, new LinkedList<>());
    }

    @Override
    public void getObserver(Node<K, V> node) {
        if (cache.containsKey(node.key)) {
            int newPointer = node.getFrequencyPointer() + 1;
            updateFrequencyList(node, newPointer, node.getFrequencyPointer());
            node.setFrequencyPointer(node.getFrequencyPointer() + 1);
            int current = counters.get(node.key);
            counters.put(node.key, current + 1);
            if (current == min && frequencyList.get(min).isEmpty()) {
                min++;
            }
        }
    }

    @Override
    public void putObserver(Node<K, V> node) {
        if (cache.containsKey(node.key)) {
            int count = counters.get(node.key);
            int newCount = count + 1;
            counters.put(node.key, newCount);
            frequencyList.get(count).remove(node);
            updateFrequencyList(node, newCount, count);
            return;
        }

        if (cache.size() == cacheCapacity) {
            Node<K, V> removed = frequencyList.get(min).removeLast();
            counters.remove(removed.key);
            cache.remove(removed.key);
        }

        min = 1;
        updateFrequencyList(node, min, 0);
        counters.put(node.key, min);
    }

    @Override
    public void removeObserver(Node<K, V> node) {
        counters.remove(node.key);
        frequencyList.get(node.frequencyPointer).remove(node);
    }

    private void updateFrequencyList(Node<K, V> node, int newPointer, int oldPointer) {
        LinkedList<Node<K, V>> elements = frequencyList.get(newPointer);
        if (elements == null) {
            frequencyList.put(newPointer, new LinkedList<>());
            frequencyList.get(newPointer).addFirst(node);
        } else {
            frequencyList.get(newPointer).addFirst(node);
        }
        if (oldPointer != 0) {
            frequencyList.get(oldPointer).remove(node);
        }
    }
}
