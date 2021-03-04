package org.dominokit.domino.plugins.resteasy;

import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.dominokit.domino.api.server.entrypoint.VertxContext;

public class ResourceContext {

    private final Vertx rxVertx;
    private final Router rxRouter;
    private final VertxContext vertxContext;
    private final RoutingContext routingContext;

    ResourceContext(Vertx rxVertx, Router rxRouter, VertxContext vertxContext, RoutingContext routingContext) {
        this.rxVertx = rxVertx;
        this.rxRouter = rxRouter;
        this.vertxContext = vertxContext;
        this.routingContext = routingContext;
    }

    public Vertx getRxVertx() {
        return rxVertx;
    }

    public Router getRxRouter() {
        return rxRouter;
    }

    public VertxContext getVertxContext() {
        return vertxContext;
    }

    public RoutingContext getRoutingContext() {
        return routingContext;
    }
}
