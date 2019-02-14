package org.dominokit.domino.api.server.config;

import org.dominokit.domino.api.server.ServerApp;
import org.dominokit.domino.api.server.entrypoint.ServerEntryPointsLoader;
import org.dominokit.domino.api.server.entrypoint.VertxContext;
import org.dominokit.domino.api.server.resource.InMemoryResourceRepository;
import org.dominokit.domino.api.server.resource.ResourcesRepository;

import java.util.ServiceLoader;

public class ServerConfigurationLoader {

    private final VertxContext serverContext;

    public ServerConfigurationLoader(VertxContext vertxContext) {
        this.serverContext = vertxContext;
    }

    public void loadModules() {
        ResourcesRepository resourcesRepository = new InMemoryResourceRepository();
        ServerApp serverApp = makeServerApp(resourcesRepository);

        ServiceLoader.load(ServerModuleConfiguration.class).forEach(serverApp::configureModule);
        ServerEntryPointsLoader.runEntryPoints(serverContext);
    }

    private ServerApp makeServerApp(ResourcesRepository resourcesRepository) {
        return new ServerApp.ServerAppBuilder()
                .handlersRepository(resourcesRepository)
                .serverContext(serverContext)
                .build();
    }
}
