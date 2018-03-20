package org.dominokit.domino.service.discovery;

import org.dominokit.domino.service.discovery.type.*;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;

import java.util.List;
import java.util.function.Function;

import static java.util.Objects.isNull;

public class VertxServiceDiscovery {

    private ServiceDiscovery serviceDiscovery;

    public VertxServiceDiscovery(Vertx vertx) {
        this.serviceDiscovery = ServiceDiscovery.create(vertx);
    }

    public ServiceDiscovery serviceDiscovery() {
        return serviceDiscovery;
    }

    public HttpServiceDiscovery http() {
        return new HttpServiceDiscovery(serviceDiscovery);
    }

    public EventBusServiceDiscovery eventBus() {
        return new EventBusServiceDiscovery(serviceDiscovery);
    }

    public MessageSourceServiceDiscovery messageSource() {
        return new MessageSourceServiceDiscovery(serviceDiscovery);
    }

    public JDBCServiceDiscovery jdbc() {
        return new JDBCServiceDiscovery(serviceDiscovery);
    }

    public RedisServiceDiscovery redis() {
        return new RedisServiceDiscovery(serviceDiscovery);
    }

    public MongoServiceDiscovery mongo() {
        return new MongoServiceDiscovery(serviceDiscovery);
    }

    public void unpublish(Record record, Handler<AsyncResult<Void>> handler) {
        serviceDiscovery.unpublish(record.getRegistration(), handler);
    }

    public void publishRecord(Record record, Handler<AsyncResult<Record>> handler) {
        serviceDiscovery.publish(record, handler);
    }

    public void lookup(Function<Record, Boolean> filter, Handler<AsyncResult<Record>> handler) {
        lookup(filter, false, handler);
    }

    public void lookupIncludeOutOfService(Function<Record, Boolean> filter, Handler<AsyncResult<Record>> handler) {
        lookup(filter, true, handler);
    }

    private void lookup(Function<Record, Boolean> filter, boolean includeOutOfService, Handler<AsyncResult<Record>> handler) {
        if (isNull(filter))
            throw new InvalidFilterException();
        serviceDiscovery.getRecord(filter, includeOutOfService, handler);
    }

    public void lookupByJson(JsonObject jsonFilter, Handler<AsyncResult<Record>> handler) {
        serviceDiscovery.getRecord(jsonFilter, handler);
    }

    public void lookupAll(Function<Record, Boolean> filter, Handler<AsyncResult<List<Record>>> handler) {
        lookupAll(filter, false, handler);
    }

    public void lookupAllIncludeOutOfService(Function<Record, Boolean> filter, Handler<AsyncResult<List<Record>>> handler) {
        lookupAll(filter, true, handler);
    }

    private void lookupAll(Function<Record, Boolean> filter, boolean includeOutOfService, Handler<AsyncResult<List<Record>>> handler) {
        if (isNull(filter))
            throw new InvalidFilterException();
        serviceDiscovery.getRecords(filter, includeOutOfService, handler);
    }

    public void lookupAllByJson(JsonObject jsonFilter, Handler<AsyncResult<List<Record>>> handler) {
        serviceDiscovery.getRecords(jsonFilter, handler);
    }

    public ServiceReference lookupForAReference(Record record) {
        return serviceDiscovery.getReference(record);
    }

    public ServiceReference lookupForAReferenceWithConfiguration(Record record, JsonObject configuration) {
        return serviceDiscovery.getReferenceWithConfiguration(record, configuration);
    }

    public void releaseServiceObject(Object object) {
        ServiceDiscovery.releaseServiceObject(serviceDiscovery, object);
    }

    public static class InvalidFilterException extends RuntimeException {
    }
}
