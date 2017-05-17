package com.progressoft.brix.domino.api.server.entrypoint;

import com.progressoft.brix.domino.api.server.config.ServerConfiguration;
import com.progressoft.brix.domino.api.server.endpoint.EndpointsRegistry;

public interface ServerContext {
    ServerConfiguration config();
    void publishEndPoint(String path, EndpointsRegistry.EndpointHandlerFactory factory);
    void publishService(String path, EndpointsRegistry.EndpointHandlerFactory factory);
    <T> T cast(Class<T> klass) throws InvalidContextTypeException;

    class InvalidContextTypeException extends Exception {
    }
}
