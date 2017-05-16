package com.progressoft.brix.domino.api.server;

import java.util.ServiceLoader;

public class ServerEntryPointsLoader {
    private ServerEntryPointsLoader() {
    }

    public static void runEntryPoints(ServerContext serverContext){
        ServiceLoader.load(ServerAppEntryPoint.class).forEach(e-> e.onModulesLoaded(serverContext));
    }
}
