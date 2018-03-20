package org.dominokit.domino.api.server.handler;

public interface HandlersRepository {

    void registerHandler(String path, RequestHandler handler);
    RequestHandler findHandler(String path);

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
