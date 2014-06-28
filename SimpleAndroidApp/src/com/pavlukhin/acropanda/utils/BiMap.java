package com.pavlukhin.acropanda.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivan on 13.06.2014.
 */
public class BiMap<K1,K2> {
    private Map<K1,K2> directMap = new HashMap<K1, K2>();
    private Map<K2,K1> reverseMap = new HashMap<K2, K1>();

    public void put(K1 key, K2 reverseKey) {
        directMap.remove(key);
        directMap.put(key, reverseKey);
        reverseMap.remove(reverseKey);
        reverseMap.put(reverseKey, key);
    }

    public K2 get(K1 key) {
        return directMap.get(key);
    }

    public K1 getReverse(K2 reverseKey) {
        return reverseMap.get(reverseKey);
    }
}
