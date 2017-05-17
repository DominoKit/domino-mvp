package com.progressoft.brix.domino.api.server.config;

import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.server.entrypoint.ServerContext;
import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointsLoader;
import com.progressoft.brix.domino.api.server.handler.HandlersRepository;
import com.progressoft.brix.domino.api.server.handler.InMemoryHandlersRepository;
import com.progressoft.brix.domino.api.server.interceptor.InMemoryInterceptorsRepository;
import com.progressoft.brix.domino.api.server.interceptor.InterceptorsRepository;
import com.progressoft.brix.domino.api.server.request.DefaultRequestExecutor;

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
