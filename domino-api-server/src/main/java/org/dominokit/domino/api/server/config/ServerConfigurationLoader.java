package org.dominokit.domino.api.server.config;

import org.dominokit.domino.api.server.ServerApp;
import org.dominokit.domino.api.server.entrypoint.ServerEntryPointsLoader;
import org.dominokit.domino.api.server.entrypoint.VertxContext;
import org.dominokit.domino.api.server.handler.HandlersRepository;
import org.dominokit.domino.api.server.handler.InMemoryHandlersRepository;
import org.dominokit.domino.api.server.interceptor.InMemoryInterceptorsRepository;
import org.dominokit.domino.api.server.interceptor.InterceptorsRepository;
import org.dominokit.domino.api.server.request.DefaultRequestExecutor;
import org.dominokit.domino.api.server.ServerApp;

import java.util.ServiceLoader;

public class ServerConfigurationLoader {

    private final VertxContext serverContext;

    public ServerConfigurationLoader(VertxContext vertxContext) {
        this.serverContext = vertxContext;
    }

    public void loadModules() {
        HandlersRepository handlersRepository = new InMemoryHandlersRepository();
        InterceptorsRepository interceptorsRepository = new InMemoryInterceptorsRepository();
        ServerApp serverApp = makeServerApp(handlersRepository, interceptorsRepository);

        ServiceLoader.load(ServerModuleConfiguration.class).forEach(serverApp::configureModule);
        ServerEntryPointsLoader.runEntryPoints(serverContext);
    }

    private ServerApp makeServerApp(HandlersRepository handlersRepository,
                                    InterceptorsRepository interceptorsRepository) {
        return new ServerApp.ServerAppBuilder()
                .handlersRepository(handlersRepository)
                .interceptorsRepository(interceptorsRepository)
                .serverContext(serverContext)
                .executor(new DefaultRequestExecutor(handlersRepository, interceptorsRepository))
                .build();
    }
}
