package org.dominokit.domino.api.server.resource;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryResourceRepository implements ResourcesRepository {
    private final List<Class<?>> handlers = new LinkedList<>();

    @Override
    public List<Class<?>> getResources() {
        return new ArrayList<>(handlers);
    }

    @Override
    public void registerResource(Class<?> resourceClass) {
        if (isResourceExists(resourceClass))
            throw new RequestHandlerHaveAlreadyBeenRegistered(resourceClass.getCanonicalName());
        handlers.add(resourceClass);
    }

    private boolean isResourceExists(Class<?> resourceClass) {
        return handlers.contains(resourceClass);
    }
}
