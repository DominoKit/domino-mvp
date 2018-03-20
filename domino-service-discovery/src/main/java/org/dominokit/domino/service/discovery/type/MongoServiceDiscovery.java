package org.dominokit.domino.service.discovery.type;

import org.dominokit.domino.service.discovery.configuration.DataSourceServiceConfiguration;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.MongoDataSource;

import java.util.function.Function;

public class MongoServiceDiscovery {
    private final ServiceDiscovery serviceDiscovery;

    public MongoServiceDiscovery(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public void publish(DataSourceServiceConfiguration configuration, Handler<AsyncResult<Record>> handler) {
        serviceDiscovery.publish(createMongoRecord(configuration), handler);
    }

    private Record createMongoRecord(DataSourceServiceConfiguration configuration) {
        return MongoDataSource.createRecord(configuration.getName(), configuration.getLocation(), configuration.getMetadata());
    }

    public void getClient(Function<Record, Boolean> filter, Handler<AsyncResult<MongoClient>> handler) {
        MongoDataSource.getMongoClient(serviceDiscovery, filter, handler);
    }

    public void getClient(JsonObject jsonFilter, Handler<AsyncResult<MongoClient>> handler) {
        MongoDataSource.getMongoClient(serviceDiscovery, jsonFilter, handler);
    }

    public void getClient(JsonObject jsonFilter, JsonObject configuration, Handler<AsyncResult<MongoClient>> handler) {
        MongoDataSource.getMongoClient(serviceDiscovery, jsonFilter, configuration, handler);
    }
}
