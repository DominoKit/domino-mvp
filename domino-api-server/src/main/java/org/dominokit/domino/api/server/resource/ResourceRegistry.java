package org.dominokit.domino.api.server.resource;

@FunctionalInterface
public interface ResourceRegistry {
    void registerResource(Class<?> resourceClass);
}
