package org.dominokit.domino.service.discovery.type;

import org.dominokit.domino.service.discovery.configuration.MessageSourceConfiguration;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.MessageSource;

import java.util.function.Function;

public class MessageSourceServiceDiscovery {
    private final ServiceDiscovery serviceDiscovery;

    public MessageSourceServiceDiscovery(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public void publish(MessageSourceConfiguration configuration, Handler<AsyncResult<Record>> handler) {
        serviceDiscovery.publish(createMessageSourceRecord(configuration), handler);
    }

    private Record createMessageSourceRecord(MessageSourceConfiguration configuration) {
        return MessageSource.createRecord(configuration.getName(),
                configuration.getAddress(), configuration.getMessageType(), configuration.getMetadata());
    }

    public <T> void getConsumer(Function<Record, Boolean> filter, Handler<AsyncResult<MessageConsumer<T>>> handler) {
        MessageSource.getConsumer(serviceDiscovery, filter, handler);
    }

    public <T> void getConsumer(JsonObject jsonFilter, Handler<AsyncResult<MessageConsumer<T>>> handler) {
        MessageSource.getConsumer(serviceDiscovery, jsonFilter, handler);
    }
}
