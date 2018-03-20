package org.dominokit.domino.api.server.request;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MultiMap<K, V> extends Iterable<Map.Entry<K, V>> {
    V get(K name);

    List<V> getAll(K name);

    List<Map.Entry<K, V>> entries();

    boolean contains(K name);

    boolean isEmpty();

    Set<K> names();

    MultiMap<K, V> add(K name, V value);

    MultiMap<K, V> add(K name, Iterable<V> values);

    MultiMap<K, V> addAll(MultiMap<K, V> map);

    MultiMap<K, V> addAll(Map<K, V> values);

    MultiMap<K, V> set(K name, V value);

    MultiMap<K, V> set(K name, Iterable<V> values);

    MultiMap<K, V> setAll(MultiMap<K, V> map);

    MultiMap<K, V> setAll(Map<K, V> values);

    MultiMap<K, V> remove(K name);

    MultiMap<K, V> clear();

    int size();
}
