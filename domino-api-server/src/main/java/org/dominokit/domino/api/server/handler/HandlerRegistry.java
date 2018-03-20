package org.dominokit.domino.api.server.handler;

@FunctionalInterface
public interface HandlerRegistry {
    void registerHandler(String request, RequestHandler handler);
}
