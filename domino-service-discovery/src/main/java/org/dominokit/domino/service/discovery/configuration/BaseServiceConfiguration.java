package org.dominokit.domino.service.discovery.configuration;

import io.vertx.core.json.JsonObject;

import static java.util.Objects.nonNull;

public abstract class BaseServiceConfiguration {

    private final String name;
    protected JsonObject metadata;

    BaseServiceConfiguration(String name) {
        if (!isValid(name))
            throw new InvalidServiceNameException();
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    protected boolean isValid(String value) {
        return nonNull(value) && !value.trim().isEmpty();
    }

    public JsonObject getMetadata() {
        return metadata;
    }

    public abstract <T extends BaseServiceConfiguration> T metadata(JsonObject metadata);

    public static class InvalidServiceNameException extends RuntimeException {
    }
}
