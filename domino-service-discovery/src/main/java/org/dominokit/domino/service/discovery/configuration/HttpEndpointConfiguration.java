package org.dominokit.domino.service.discovery.configuration;

import io.vertx.core.json.JsonObject;

public class HttpEndpointConfiguration extends BaseServiceConfiguration {
    private final String host;
    private String root = "/";
    private int port = 80;
    private boolean ssl = false;

    public HttpEndpointConfiguration(String name, String host) {
        super(name);
        if (!isValid(host))
            throw new InvalidHttpEndpointHostException();
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public HttpEndpointConfiguration root(String root) {
        this.root = root;
        return this;
    }

    public HttpEndpointConfiguration port(int port) {
        this.port = port;
        return this;
    }

    public HttpEndpointConfiguration ssl(boolean ssl) {
        this.ssl = ssl;
        return this;
    }

    public boolean isSsl() {
        return ssl;
    }

    public String getRoot() {
        return root;
    }

    public int getPort() {
        return port;
    }

    @Override
    public HttpEndpointConfiguration metadata(JsonObject metadata) {
        this.metadata = metadata;
        return this;
    }

    public static class InvalidHttpEndpointHostException extends RuntimeException {
    }
}
