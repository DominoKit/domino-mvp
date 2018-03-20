package org.dominokit.domino.api.server.handler;

import java.util.HashMap;
import java.util.Map;

public class InMemoryHandlersRepository implements HandlersRepository {
    private final Map<PathMatcher, RequestHandler> handlers = new HashMap<>();

    @Override
    public void registerHandler(String path, RequestHandler handler) {
        if (isPathExist(path))
            throw new RequestHandlerHaveAlreadyBeenRegistered("Path  : " + path);
        handlers.put(new PathMatcher(path), handler);
    }

    private boolean isPathExist(String path) {
        return handlers.entrySet().stream()
                .anyMatch(entry -> isMatchPath(path, entry));
    }

    @Override
    public RequestHandler findHandler(String path) {
        return handlers.entrySet().stream()
                .filter(entry -> isMatchPath(path, entry))
                .findFirst()
                .orElseThrow(() -> new RequestHandlerNotFound("Request : " + path))
                .getValue();
    }

    private boolean isMatchPath(String request, Map.Entry<PathMatcher, RequestHandler> entry) {
        return entry.getKey().isMatch(request);
    }
}
