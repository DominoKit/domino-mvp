package org.dominokit.domino.api.server.config;

import java.time.Instant;
import java.util.Set;

public interface ServerConfiguration<T,A> {
    String getString(String key);

    Integer getInteger(String key);

    Long getLong(String key);

    Double getDouble(String key);

    Float getFloat(String key);

    Boolean getBoolean(String key);

    T getJsonObject(String key);

    A getJsonArray(String key);

    byte[] getBinary(String key);

    Instant getInstant(String key);

    Object getValue(String key);

    String getString(String key, String def);

    Integer getInteger(String key, Integer def);

    Long getLong(String key, Long def);

    Double getDouble(String key, Double def);

    Float getFloat(String key, Float def);

    Boolean getBoolean(String key, Boolean def);

    T getJsonObject(String key, T def);

    A getJsonArray(String key, A def);

    byte[] getBinary(String key, byte[] def);

    Instant getInstant(String key, Instant def);

    Object getValue(String key, Object def);

    boolean containsKey(String key);

    Set<String> fieldNames();

    ServerConfiguration put(String key, Enum value);

    ServerConfiguration put(String key, CharSequence value);

    ServerConfiguration put(String key, String value);

    ServerConfiguration put(String key, Integer value);

    ServerConfiguration put(String key, Long value);

    ServerConfiguration put(String key, Double value);

    ServerConfiguration put(String key, Float value);

    ServerConfiguration put(String key, Boolean value);

    ServerConfiguration putNull(String key);

    ServerConfiguration put(String key, byte[] value);

    ServerConfiguration put(String key, Instant value);

    ServerConfiguration put(String key, Object value);

    Object remove(String key);

    boolean isEmpty();

}
