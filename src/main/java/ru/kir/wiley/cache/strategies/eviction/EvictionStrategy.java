package ru.kir.wiley.cache.strategies.eviction;

import java.util.Objects;

/**
 * Created by Kirill Zhitelev on 27.10.2019.
 */
public interface EvictionStrategy<K, V> {
    void getObserver(Node<K, V> node);

    void putObserver(Node<K, V> node);

    void removeObserver(Node<K, V> node);

    enum EvictionStrategyType {
        LRU,
        LFU
    }

    class Node<K, V> {
        K key;
        V value;
        int frequencyPointer = 1;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return Objects.equals(key, node.key) &&
                    Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public int getFrequencyPointer() {
            return frequencyPointer;
        }

        public void setFrequencyPointer(int frequencyPointer) {
            this.frequencyPointer = frequencyPointer;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }
}
