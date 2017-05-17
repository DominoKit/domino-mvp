package com.progressoft.brix.domino.api.server.handler;

public interface HandlersRepository {

    void registerHandler(String request, RequestHandler handler);
    void registerCallbackHandler(String request, CallbackRequestHandler handler);
    RequestHandler findHandler(String request);

    CallbackRequestHandler findCallbackHandler(String canonicalName);

    class RequestHandlerHaveAlreadyBeenRegistered extends RuntimeException {
        public RequestHandlerHaveAlreadyBeenRegistered(String message) {
            super(message);
        }
    }

    class RequestHandlerNotFound extends RuntimeException {
        public RequestHandlerNotFound(String message) {
            super(message);
        }
    }

    class RequestCallbackHandlerNotFound extends RuntimeException {
        public RequestCallbackHandlerNotFound(String message) {
            super(message);
        }
    }


}
