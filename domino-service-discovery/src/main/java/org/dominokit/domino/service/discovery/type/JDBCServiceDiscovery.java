package org.dominokit.domino.service.discovery.type;

import org.dominokit.domino.service.discovery.configuration.DataSourceServiceConfiguration;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.JDBCDataSource;

import java.util.function.Function;

public class JDBCServiceDiscovery {
    private final ServiceDiscovery serviceDiscovery;

    public JDBCServiceDiscovery(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public void publish(DataSourceServiceConfiguration configuration, Handler<AsyncResult<Record>> handler) {
        serviceDiscovery.publish(createJDBCRecord(configuration), handler);
    }

    private Record createJDBCRecord(DataSourceServiceConfiguration configuration) {
        return JDBCDataSource.createRecord(configuration.getName(), configuration.getLocation(), configuration.getMetadata());
    }

    public void getClient(Function<Record, Boolean> filter, Handler<AsyncResult<JDBCClient>> handler) {
        JDBCDataSource.getJDBCClient(serviceDiscovery, filter, handler);
    }

    public void getClient(Function<Record, Boolean> filter, JsonObject configuration, Handler<AsyncResult<JDBCClient>> handler) {
        JDBCDataSource.getJDBCClient(serviceDiscovery, filter, configuration, handler);
    }

    public void getClient(JsonObject jsonFilter, Handler<AsyncResult<JDBCClient>> handler) {
        JDBCDataSource.getJDBCClient(serviceDiscovery, jsonFilter, handler);
    }

    public void getClient(JsonObject jsonFilter, JsonObject configuration, Handler<AsyncResult<JDBCClient>> handler) {
        JDBCDataSource.getJDBCClient(serviceDiscovery, jsonFilter, configuration, handler);
    }
}
