package ru.kir.wiley.cache.impl;

import ru.kir.wiley.cache.Cache;
import ru.kir.wiley.cache.strategies.eviction.EvictionStrategy;

/**
 * Created by Kirill Zhitelev on 26.11.2019.
 */
public class ComplicatedCache implements Cache<String, String> {
    @Override
    public void putObject(String key, String value) {
        
    }

    @Override
    public String getObject(String key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void removeObject(String key) {

    }

    @Override
    public void clear() {

    }

    @Override
    public boolean containsValue(EvictionStrategy.Node<String, String> value) {
        return false;
    }

    @Override
    public boolean containsKey(String key) {
        return false;
    }
}
