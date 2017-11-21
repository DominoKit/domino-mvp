package com.progressoft.brix.domino.api.server.request;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MultiValuesMap<K, V> extends Iterable<Map.Entry<K, V>> {
    V get(K name);

    List<V> getAll(K name);

    List<Map.Entry<K, V>> entries();

    boolean contains(K name);

    boolean isEmpty();

    Set<K> names();

    MultiValuesMap<K, V> add(K name, V value);

    MultiValuesMap<K, V> add(K name, Iterable<V> values);

    MultiValuesMap<K, V> addAll(MultiValuesMap<K, V> map);

    MultiValuesMap<K, V> addAll(Map<K, V> values);

    MultiValuesMap<K, V> set(K name, V value);

    MultiValuesMap<K, V> set(K name, Iterable<V> values);

    MultiValuesMap<K, V> setAll(MultiValuesMap<K, V> map);

    MultiValuesMap<K, V> setAll(Map<K, V> values);

    MultiValuesMap<K, V> remove(K name);

    MultiValuesMap<K, V> clear();

    int size();
}
