package org.dominokit.domino.api.server.config;

import org.dominokit.domino.api.server.ServerApp;
import org.dominokit.domino.api.server.ServerApp;

public final class ServerModuleConfigurator {

    public void configureModule(ServerModuleConfiguration configuration){
        ServerApp.make().configureModule(configuration);
    }

}
