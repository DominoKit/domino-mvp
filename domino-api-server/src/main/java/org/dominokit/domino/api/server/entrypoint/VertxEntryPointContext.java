package org.dominokit.domino.api.server.entrypoint;

import org.dominokit.domino.api.server.config.ServerConfiguration;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

public class VertxEntryPointContext implements ServerEntryPointContext {

    private final RoutingContext routingContext;
    private final ServerConfiguration serverConfiguration;
    private final Vertx vertx;

    public VertxEntryPointContext(RoutingContext routingContext,
                                  ServerConfiguration serverConfiguration, Vertx vertx) {
        this.routingContext = routingContext;
        this.serverConfiguration = serverConfiguration;
        this.vertx = vertx;
    }

    public RoutingContext getRoutingContext() {
        return routingContext;
    }

    public ServerConfiguration config(){
        return serverConfiguration;
    }

    public Vertx vertx(){
        return vertx;
    }

    @Override
    public String getName() {
        return VertxEntryPointContext.class.getCanonicalName();
    }
}
