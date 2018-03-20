package org.dominokit.domino.service.discovery.type;

import org.dominokit.domino.service.discovery.configuration.DataSourceServiceConfiguration;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.RedisDataSource;

import java.util.function.Function;

public class RedisServiceDiscovery {
    private final ServiceDiscovery serviceDiscovery;

    public RedisServiceDiscovery(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public void publish(DataSourceServiceConfiguration configuration, Handler<AsyncResult<Record>> handler) {
        serviceDiscovery.publish(createRedisRecord(configuration), handler);
    }

    private Record createRedisRecord(DataSourceServiceConfiguration configuration) {
        return RedisDataSource.createRecord(configuration.getName(), configuration.getLocation(), configuration.getMetadata());
    }

    public void getClient(Function<Record, Boolean> filter, Handler<AsyncResult<RedisClient>> handler) {
        RedisDataSource.getRedisClient(serviceDiscovery, filter, handler);
    }

    public void getClient(Function<Record, Boolean> filter, JsonObject configuration, Handler<AsyncResult<RedisClient>> handler) {
        RedisDataSource.getRedisClient(serviceDiscovery, filter, configuration, handler);
    }

    public void getClient(JsonObject jsonFilter, Handler<AsyncResult<RedisClient>> handler) {
        RedisDataSource.getRedisClient(serviceDiscovery, jsonFilter, handler);
    }

    public void getClient(JsonObject jsonFilter, JsonObject configuration, Handler<AsyncResult<RedisClient>> handler) {
        RedisDataSource.getRedisClient(serviceDiscovery, jsonFilter, configuration, handler);
    }
}
