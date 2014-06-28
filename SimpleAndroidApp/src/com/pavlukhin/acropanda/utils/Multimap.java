package com.pavlukhin.acropanda.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ivan on 27.05.2014.
 */
public class Multimap<K,V> {
    private Map<K, List<V>> internalMap = new HashMap<K, List<V>>();

    public void put(K key, V value) {
        List<V> values = internalMap.get(key);
        if(values == null) {
            values = new ArrayList<V>();
            internalMap.put(key, values);
        }
        values.add(value);
    }

    public List<V> get(K key) {
        return internalMap.get(key);
    }

    public Set<K> keySet() {
        return internalMap.keySet();
    }
}
