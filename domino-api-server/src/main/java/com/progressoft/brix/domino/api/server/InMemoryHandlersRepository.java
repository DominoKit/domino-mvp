package com.progressoft.brix.domino.api.server;


import java.util.HashMap;
import java.util.Map;

public class InMemoryHandlersRepository implements HandlersRepository {
    private final Map<String, RequestHandler> handlers = new HashMap<>();
    private final Map<String, CallbackRequestHandler> callbackHandlers = new HashMap<>();

    @Override
    public void registerHandler(String request, RequestHandler handler) {
        if (handlers.containsKey(request))
            throw new RequestHandlerHaveAlreadyBeenRegistered("Request  : " + request);
        handlers.put(request, handler);
    }

    @Override
    public void registerCallbackHandler(String request, CallbackRequestHandler handler) {
        if (callbackHandlers.containsKey(request))
            throw new RequestHandlerHaveAlreadyBeenRegistered("Request  : " + request);
        callbackHandlers.put(request, handler);
    }

    @Override
    public RequestHandler findHandler(String request) {
        if (handlers.containsKey(request))
            return handlers.get(request);
        throw new RequestHandlerNotFound("Request : " + request);
    }

    @Override
    public CallbackRequestHandler findCallbackHandler(String request) {
        if (callbackHandlers.containsKey(request))
            return callbackHandlers.get(request);
        throw new RequestCallbackHandlerNotFound("Request : " + request);
    }
}
