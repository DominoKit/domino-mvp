package org.dominokit.domino.api.server.entrypoint;

@FunctionalInterface
public interface ServerAppEntryPoint<T extends ServerContext> {
    void onModulesLoaded(T context);
}
