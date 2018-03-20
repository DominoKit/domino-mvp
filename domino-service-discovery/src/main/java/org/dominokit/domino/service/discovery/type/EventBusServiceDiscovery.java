package org.dominokit.domino.service.discovery.type;

import org.dominokit.domino.service.discovery.configuration.EventBusServiceConfiguration;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.EventBusService;

import java.util.function.Function;

public class EventBusServiceDiscovery {
    private final ServiceDiscovery serviceDiscovery;

    public EventBusServiceDiscovery(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public void publish(EventBusServiceConfiguration configuration, Handler<AsyncResult<Record>> handler) {
        serviceDiscovery.publish(createEventBusRecord(configuration), handler);
    }

    private Record createEventBusRecord(EventBusServiceConfiguration configuration) {
        return EventBusService.createRecord(configuration.getName(),
                configuration.getAddress(),
                configuration.getServiceClass(),
                configuration.getMetadata());
    }

    public <T> void getProxy(Class<T> serviceClass, Handler<AsyncResult<T>> handler) {
        EventBusService.getProxy(serviceDiscovery, serviceClass, handler);
    }

    public <T> void getServiceProxy(Function<Record, Boolean> filter, Class<T> serviceClass, Handler<AsyncResult<T>> handler) {
        EventBusService.getServiceProxy(serviceDiscovery, filter, serviceClass, handler);
    }

    public <T> void getServiceProxy(JsonObject jsonFilter, Class<T> serviceClass, Handler<AsyncResult<T>> handler) {
        EventBusService.getServiceProxyWithJsonFilter(serviceDiscovery, jsonFilter, serviceClass, handler);
    }
}
