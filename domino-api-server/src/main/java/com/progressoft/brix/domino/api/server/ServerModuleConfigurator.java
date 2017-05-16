package com.progressoft.brix.domino.api.server;

public final class ServerModuleConfigurator {

    public void configureModule(ServerModuleConfiguration configuration){
        ServerApp.make().configureModule(configuration);
    }

}
