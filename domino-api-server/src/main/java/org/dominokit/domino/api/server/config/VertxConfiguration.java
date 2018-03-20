package org.dominokit.domino.api.server.config;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.time.Instant;
import java.util.Set;

public class VertxConfiguration implements ServerConfiguration<JsonObject, JsonArray> {

    private final JsonObject jsonObject;

    public VertxConfiguration(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public String getString(String key) {
        return jsonObject.getString(key);
    }

    @Override
    public Integer getInteger(String key) {
        return jsonObject.getInteger(key);
    }

    @Override
    public Long getLong(String key) {
        return jsonObject.getLong(key);
    }

    @Override
    public Double getDouble(String key) {
        return jsonObject.getDouble(key);
    }

    @Override
    public Float getFloat(String key) {
        return jsonObject.getFloat(key);
    }

    @Override
    public Boolean getBoolean(String key) {
        return jsonObject.getBoolean(key);
    }

    @Override
    public JsonObject getJsonObject(String key) {
        return jsonObject.getJsonObject(key);
    }

    @Override
    public JsonArray getJsonArray(String key) {
        return jsonObject.getJsonArray(key);
    }

    @Override
    public byte[] getBinary(String key) {
        return jsonObject.getBinary(key);
    }

    @Override
    public Instant getInstant(String key) {
        return jsonObject.getInstant(key);
    }

    @Override
    public Object getValue(String key) {
        return jsonObject.getValue(key);
    }

    @Override
    public String getString(String key, String def) {
        return jsonObject.getString(key, def);
    }

    @Override
    public Integer getInteger(String key, Integer def) {
        return jsonObject.getInteger(key, def);
    }

    @Override
    public Long getLong(String key, Long def) {
        return jsonObject.getLong(key, def);
    }

    @Override
    public Double getDouble(String key, Double def) {
        return jsonObject.getDouble(key,def);
    }

    @Override
    public Float getFloat(String key, Float def) {
        return jsonObject.getFloat(key, def);
    }

    @Override
    public Boolean getBoolean(String key, Boolean def) {
        return jsonObject.getBoolean(key, def);
    }

    @Override
    public JsonObject getJsonObject(String key, JsonObject def) {
        return jsonObject.getJsonObject(key, def);
    }

    @Override
    public JsonArray getJsonArray(String key, JsonArray def) {
        return jsonObject.getJsonArray(key, def);
    }

    @Override
    public byte[] getBinary(String key, byte[] def) {
        return jsonObject.getBinary(key, def);
    }

    @Override
    public Instant getInstant(String key, Instant def) {
        return jsonObject.getInstant(key, def);
    }

    @Override
    public Object getValue(String key, Object def) {
        return jsonObject.getValue(key, def);
    }

    @Override
    public boolean containsKey(String key) {
        return jsonObject.containsKey(key);
    }

    @Override
    public Set<String> fieldNames() {
        return jsonObject.fieldNames();
    }

    @Override
    public ServerConfiguration put(String key, Enum value) {
        jsonObject.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, CharSequence value) {
        jsonObject.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, String value) {
        jsonObject.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, Integer value) {
        jsonObject.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, Long value) {
        jsonObject.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, Double value) {
        jsonObject.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, Float value) {
        jsonObject.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, Boolean value) {
        jsonObject.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration putNull(String key) {
        jsonObject.putNull(key);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, byte[] value) {
        jsonObject.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, Instant value) {
        jsonObject.put(key, value);
        return this;
    }

    @Override
    public ServerConfiguration put(String key, Object value) {
        jsonObject.put(key, value);
        return this;
    }

    @Override
    public Object remove(String key) {
        return jsonObject.remove(key);
    }

    @Override
    public boolean isEmpty() {
        return jsonObject.isEmpty();
    }
}
