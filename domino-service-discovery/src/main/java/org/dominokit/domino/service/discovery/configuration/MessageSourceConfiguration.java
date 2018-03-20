package org.dominokit.domino.service.discovery.configuration;

import io.vertx.core.json.JsonObject;

public class MessageSourceConfiguration extends AddressableServiceConfiguration {
    private Class<?> messageType;

    public MessageSourceConfiguration(String name, String address) {
        super(name, address);
    }

    public MessageSourceConfiguration messageType(Class<?> messageType) {
        this.messageType = messageType;
        return this;
    }

    public Class<?> getMessageType() {
        return messageType;
    }

    @Override
    public MessageSourceConfiguration metadata(JsonObject metadata) {
        this.metadata = metadata;
        return this;
    }
}
