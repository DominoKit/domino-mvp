package org.dominokit.domino.api.server.request;

import java.util.*;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;

public class DefaultMultiMap<K, V> implements MultiMap<K, V> {

    protected Map<K, List<V>> values = new LinkedHashMap<>();

    @Override
    public V get(K name) {
        if (!contains(name))
            return null;
        return getAll(name).get(0);
    }

    @Override
    public List<V> getAll(K name) {
        if (!contains(name))
            return new LinkedList<>();
        return new LinkedList<>(values.get(name));
    }

    @Override
    public List<Map.Entry<K, V>> entries() {
        return values.keySet()
                .stream()
                .filter(key -> nonNull(values.get(key)))
                .map(this::asEntries)
                .flatMap(Collection::stream)
                .collect(toCollection(LinkedList::new));
    }

    private List<Entry> asEntries(K key) {
        return getAll(key).stream().map(value -> new Entry(key, value)).collect(toCollection(LinkedList::new));
    }

    @Override
    public boolean contains(K name) {
        return values.containsKey(name);
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public Set<K> names() {
        return values.keySet();
    }

    @Override
    public MultiMap<K, V> add(K name, V value) {
        requireNonNull(name);
        requireNonNull(value);
        if (!contains(name))
            values.put(name, new ArrayList<>());
        values.get(name).add(value);
        return this;
    }

    @Override
    public MultiMap<K, V> add(K name, Iterable<V> values) {
        requireNonNull(values);
        values.forEach(value -> add(name, value));
        return this;
    }

    @Override
    public MultiMap<K, V> addAll(MultiMap<K, V> map) {
        requireNonNull(map);
        map.entries().forEach(entry -> this.add(entry.getKey(), entry.getValue()));
        return this;
    }

    @Override
    public MultiMap<K, V> addAll(Map<K, V> values) {
        requireNonNull(values);
        values.forEach(this::add);
        return this;
    }

    @Override
    public MultiMap<K, V> set(K name, V value) {
        requireNonNull(name);
        requireNonNull(value);
        clearItem(name);
        add(name, value);
        return this;
    }

    @Override
    public MultiMap<K, V> set(K name, Iterable<V> values) {
        clearItem(name);
        add(name, values);
        return this;
    }

    private void clearItem(K name) {
        values.put(name, new LinkedList<>());
    }

    @Override
    public MultiMap<K, V> setAll(MultiMap<K, V> map) {
        requireNonNull(map);
        clear();
        addAll(map);
        return this;
    }

    @Override
    public MultiMap<K, V> setAll(Map<K, V> values) {
        requireNonNull(values);
        clear();
        addAll(values);
        return this;
    }

    @Override
    public MultiMap<K, V> remove(K name) {
        requireNonNull(name);
        values.remove(name);
        return this;
    }

    @Override
    public MultiMap<K, V> clear() {
        values = new LinkedHashMap<>();
        return this;
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return entries().iterator();
    }

    private class Entry implements Map.Entry<K, V> {

        private K key;
        private V value;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || !(o instanceof Map.Entry)) return false;

            Map.Entry entry = (Map.Entry) o;

            if (key != null ? !key.equals(entry.getKey()) : entry.getKey() != null) return false;
            return value != null ? value.equals(entry.getValue()) : entry.getValue() == null;
        }

        @Override
        public int hashCode() {
            int result = key != null ? key.hashCode() : 0;
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Entry[" +
                    "key=\"" + key + "\"" +
                    ", value=\"" + value + "\"" +
                    "]";
        }
    }
}
