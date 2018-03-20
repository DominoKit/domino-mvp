package org.dominokit.domino.service.discovery.type;

import org.dominokit.domino.service.discovery.configuration.HttpEndpointConfiguration;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;

import java.util.function.Function;

public class HttpServiceDiscovery {
    private final ServiceDiscovery serviceDiscovery;

    public HttpServiceDiscovery(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public void publish(HttpEndpointConfiguration configuration, Handler<AsyncResult<Record>> handler) {
        serviceDiscovery.publish(createHttpEndpointRecord(configuration), handler);
    }

    private Record createHttpEndpointRecord(HttpEndpointConfiguration configuration) {
        return HttpEndpoint.createRecord(configuration.getName(),
                configuration.isSsl(),
                configuration.getHost(),
                configuration.getPort(),
                configuration.getRoot(),
                configuration.getMetadata());
    }

    public void getHttpClient(Function<Record, Boolean> filter, Handler<AsyncResult<HttpClient>> handler) {
        HttpEndpoint.getClient(serviceDiscovery, filter, handler);
    }

    public void getHttpClient(Function<Record, Boolean> filter, JsonObject configuration, Handler<AsyncResult<HttpClient>> handler) {
        HttpEndpoint.getClient(serviceDiscovery, filter, configuration, handler);
    }

    public void getHttpClient(JsonObject jsonFilter, Handler<AsyncResult<HttpClient>> handler) {
        HttpEndpoint.getClient(serviceDiscovery, jsonFilter, handler);
    }

    public void getHttpClient(JsonObject jsonFilter, JsonObject configuration, Handler<AsyncResult<HttpClient>> handler) {
        HttpEndpoint.getClient(serviceDiscovery, jsonFilter, configuration, handler);
    }

    public void getWebClient(Function<Record, Boolean> filter, Handler<AsyncResult<WebClient>> handler) {
        HttpEndpoint.getWebClient(serviceDiscovery, filter, handler);
    }

    public void getWebClient(Function<Record, Boolean> filter, JsonObject configuration, Handler<AsyncResult<WebClient>> handler) {
        HttpEndpoint.getWebClient(serviceDiscovery, filter, configuration, handler);
    }

    public void getWebClient(JsonObject jsonFilter, Handler<AsyncResult<WebClient>> handler) {
        HttpEndpoint.getWebClient(serviceDiscovery, jsonFilter, handler);
    }

    public void getWebClient(JsonObject jsonFilter, JsonObject configuration, Handler<AsyncResult<WebClient>> handler) {
        HttpEndpoint.getWebClient(serviceDiscovery, jsonFilter, configuration, handler);
    }
}
