package com.progressoft.brix.domino.api.server;

@FunctionalInterface
public interface ServerAppEntryPoint {
    void onModulesLoaded(ServerContext context);
}
