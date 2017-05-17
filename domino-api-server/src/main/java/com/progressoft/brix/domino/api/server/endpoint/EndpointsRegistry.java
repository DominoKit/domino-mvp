package com.progressoft.brix.domino.api.server.endpoint;

@FunctionalInterface
public interface EndpointsRegistry {

    @FunctionalInterface
    interface EndpointHandlerFactory {
        <T> T get();
    }

    void registerEndpoint(String path, EndpointHandlerFactory factory);
}
