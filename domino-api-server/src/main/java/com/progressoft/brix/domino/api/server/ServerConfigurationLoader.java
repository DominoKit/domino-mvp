package com.progressoft.brix.domino.api.server;

import java.util.ServiceLoader;

public class ServerConfigurationLoader {

    private final ServerContext serverContext;

    public ServerConfigurationLoader(ServerContext serverContext) {
        this.serverContext = serverContext;
    }

    public void loadModules(){
        HandlersRepository handlersRepository=new InMemoryHandlersRepository();
        InterceptorsRepository interceptorsRepository=new InMemoryInterceptorsRepository();
        ServerApp serverApp= makeServerApp(handlersRepository, interceptorsRepository);

        ServiceLoader.load(ServerModuleConfiguration.class).forEach(serverApp::configureModule);

        ServerEntryPointsLoader.runEntryPoints(serverContext);
    }

    private ServerApp makeServerApp(HandlersRepository handlersRepository,
                                    InterceptorsRepository interceptorsRepository) {
        return new ServerApp.ServerAppBuilder().handlersRepository(handlersRepository).interceptorsRepository(interceptorsRepository).serverContext(serverContext).executor(new DefaultRequestExecutor(handlersRepository, interceptorsRepository)).build();
    }

}
