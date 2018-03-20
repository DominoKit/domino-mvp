package org.dominokit.domino.service.discovery.configuration;

import io.vertx.core.json.JsonObject;

import static java.util.Objects.isNull;

public class EventBusServiceConfiguration extends AddressableServiceConfiguration {
    private Class<?> serviceClass;

    public EventBusServiceConfiguration(String name, String address, Class<?> serviceClass) {
        super(name, address);
        if (isNull(serviceClass))
            throw new NullServiceClassException();
        this.serviceClass = serviceClass;
    }

    public Class<?> getServiceClass() {
        return serviceClass;
    }

    @Override
    public EventBusServiceConfiguration metadata(JsonObject metadata) {
        this.metadata = metadata;
        return this;
    }

    public static class NullServiceClassException extends RuntimeException {
    }
}
