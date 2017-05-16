package com.progressoft.brix.domino.api.server;

public interface HandlerRegistry {
    void registerHandler(String request, RequestHandler handler);
    void registerCallbackHandler(String request, CallbackRequestHandler handler);
}
