package com.progressoft.brix.domino.api.server;

import io.vertx.ext.web.RoutingContext;

public class VertxEntryPointContext implements ServerEntryPointContext {

    private final RoutingContext routingContext;
    private final ServerConfiguration serverConfiguration;

    public VertxEntryPointContext(RoutingContext routingContext,
                                  ServerConfiguration serverConfiguration) {
        this.routingContext = routingContext;
        this.serverConfiguration = serverConfiguration;
    }

    public RoutingContext getRoutingContext() {
        return routingContext;
    }

    public ServerConfiguration config(){
        return serverConfiguration;
    }

    @Override
    public String getName() {
        return "VERTX";
    }
}
