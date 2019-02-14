package org.dominokit.domino.api.server.resource;

import java.util.List;

public interface ResourcesRepository {

    void registerResource(Class<?> resourceClasses);
    List<Class<?>> getResources();

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
