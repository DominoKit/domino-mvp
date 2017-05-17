package com.progressoft.brix.domino.api.server.entrypoint;

@FunctionalInterface
public interface ServerAppEntryPoint {
    void onModulesLoaded(ServerContext context);
}
