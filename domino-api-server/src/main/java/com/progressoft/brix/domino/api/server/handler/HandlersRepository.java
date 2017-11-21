package com.progressoft.brix.domino.api.server.handler;

public interface HandlersRepository {

    void registerHandler(String request, RequestHandler handler);
    RequestHandler findHandler(String request);

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
}
