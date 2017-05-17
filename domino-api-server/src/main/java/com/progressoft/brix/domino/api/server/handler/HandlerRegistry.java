package com.progressoft.brix.domino.api.server.handler;

public interface HandlerRegistry {
    void registerHandler(String request, RequestHandler handler);
    void registerCallbackHandler(String request, CallbackRequestHandler handler);
}
