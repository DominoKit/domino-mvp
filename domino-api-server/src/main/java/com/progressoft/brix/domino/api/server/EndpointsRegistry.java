package com.progressoft.brix.domino.api.server;

@FunctionalInterface
public interface EndpointsRegistry {

    @FunctionalInterface
    interface EndpointHandlerFactory {
        <T> T get();
    }

    void registerEndpoint(String path, EndpointHandlerFactory factory);
}
