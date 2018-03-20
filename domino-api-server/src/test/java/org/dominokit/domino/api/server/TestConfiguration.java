package org.dominokit.domino.api.server;

import org.dominokit.domino.api.server.config.ServerConfiguration;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TestConfiguration implements ServerConfiguration<Object, Object[]> {
    private final Map<String, Object> config = new HashMap<>();

    @Override
    public String getString(String key) {
        return (String) config.get(key);
    }

    @Override
    public Integer getInteger(String key) {
        return (Integer) config.get(key);
    }

    @Override
    public Long getLong(String key) {
        return (Long) config.get(key);
    }

    @Override
    public Double getDouble(String key) {
        return (Double) config.get(key);
    }

    @Override
    public Float getFloat(String key) {
        return (Float) config.get(key);
    }

    @Override
    public Boolean getBoolean(String key) {
        return (Boolean) config.get(key);
    }

    @Override
    public Object getJsonObject(String key) {
        return getValue(key);
    }

    @Override
    public Object[] getJsonArray(String key) {
        return (Object[]) config.get(key);
    }

    @Override
    public byte[] getBinary(String key) {
        return (byte[]) config.get(key);
    }

    @Override
    public Instant getInstant(String key) {
        return (Instant) config.get(key);
    }

    @Override
    public Object getValue(String key) {
        return config.get(key);
    }

    @Override
    public String getString(String key, String def) {
        if (config.containsKey(key))
            return getString(key);
        return def;
    }

    @Override
    public Integer getInteger(String key, Integer def) {
        if (config.containsKey(key))
            return getInteger(key);
        return def;
    }

    @Override
    public Long getLong(String key, Long def) {
        if (config.containsKey(key))
            return getLong(key);
        return def;
    }

    @Override
    public Double getDouble(String key, Double def) {
        if (config.containsKey(key))
            return getDouble(key);
        return def;
    }

    @Override
    public Float getFloat(String key, Float def) {
        if (config.containsKey(key))
            return getFloat(key);
        return def;
    }

    @Override
    public Boolean getBoolean(String key, Boolean def) {
        if (config.containsKey(key))
            return getBoolean(key);
        return def;
    }

    @Override
    public Object getJsonObject(String key, Object def) {
        if (config.containsKey(key))
            return getJsonObject(key);
        return def;
    }

    @Override
    public Object[] getJsonArray(String key, Object[] def) {
        if (config.containsKey(key))
            return getJsonArray(key);
        return def;
    }

    @Override
    public byte[] getBinary(String key, byte[] def) {
        if (config.containsKey(key))
            return getBinary(key);
        return def;
    }

    @Override
    public Instant getInstant(String key, Instant def) {
        if (config.containsKey(key))
            return getInstant(key);
        return def;
    }

    @Override
    public Object getValue(String key, Object def) {
        if (config.containsKey(key))
            return getValue(key);
        return def;
    }

    @Override
    public boolean containsKey(String key) {
        return config.containsKey(key);
    }

    @Override
    public Set<String> fieldNames() {
        return config.keySet();
    }

    @Override
    public ServerConfiguration put(String key, Enum value) {
        config.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, CharSequence value) {
        config.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, String value) {
        config.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, Integer value) {
        config.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, Long value) {
        config.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, Double value) {
        config.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, Float value) {
        config.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, Boolean value) {
        config.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration putNull(String key) {
        config.put(key, null);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, byte[] value) {
        config.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, Instant value) {
        config.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, Object value) {
        config.put(key, value);
        return this;
    }

    @Override
    public Object remove(String key) {
        return config.remove(key);
    }

    @Override
    public boolean isEmpty() {
        return config.isEmpty();
    }
}
