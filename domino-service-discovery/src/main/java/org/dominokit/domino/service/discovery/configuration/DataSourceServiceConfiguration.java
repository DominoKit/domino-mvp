package org.dominokit.domino.service.discovery.configuration;

import io.vertx.core.json.JsonObject;

import static java.util.Objects.isNull;

public class DataSourceServiceConfiguration extends BaseServiceConfiguration {
    private JsonObject location;

    public DataSourceServiceConfiguration(String name, JsonObject location) {
        super(name);
        if (isNull(location))
            throw new NullServiceLocationException();
        this.location = location;
    }

    public JsonObject getLocation() {
        return location;
    }

    @Override
    public DataSourceServiceConfiguration metadata(JsonObject metadata) {
        this.metadata = metadata;
        return this;
    }

    public static class NullServiceLocationException extends RuntimeException {
    }
}
