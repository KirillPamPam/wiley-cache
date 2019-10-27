package ru.kir.wiley.cache.strategies.eviction;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Kirill Zhitelev on 27.10.2019.
 */
public final class LRUStrategy<K, V> implements EvictionStrategy<K, V> {
    private LinkedList<Node<K, V>> cacheElementsList = new LinkedList<>();
    private Map<K, Node<K, V>> cache;
    private int cacheCapacity;

    public LRUStrategy(Map<K, Node<K, V>> cache, int cacheCapacity) {
        this.cache = cache;
        this.cacheCapacity = cacheCapacity;
    }

    @Override
    public void getObserver(EvictionStrategy.Node<K, V> node) {
        if (cache.containsKey(node.key)) {
            moveToHead(node, node);
        }
    }

    @Override
    public void putObserver(Node<K, V> node) {
        if (cache.containsKey(node.key)) {
            moveToHead(node, cache.get(node.key));
            return;
        }

        if (cache.size() == cacheCapacity) {
            Node<K, V> removed = cacheElementsList.removeLast();
            cache.remove(removed.key);
        }
        cacheElementsList.addFirst(node);
    }

    @Override
    public void removeObserver(Node<K, V> node) {
        cacheElementsList.remove(node);
    }

    private void moveToHead(Node<K, V> newNode, Node<K, V> oldNode) {
        cacheElementsList.remove(oldNode);
        cacheElementsList.addFirst(newNode);
    }
}
