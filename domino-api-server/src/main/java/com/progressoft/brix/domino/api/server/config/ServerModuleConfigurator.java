package com.progressoft.brix.domino.api.server.config;

import com.progressoft.brix.domino.api.server.ServerApp;

public final class ServerModuleConfigurator {

    public void configureModule(ServerModuleConfiguration configuration){
        ServerApp.make().configureModule(configuration);
    }

}
