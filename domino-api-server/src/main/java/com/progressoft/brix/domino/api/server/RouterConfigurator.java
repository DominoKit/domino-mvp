package com.progressoft.brix.domino.api.server;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CSRFHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;

import static java.util.Objects.isNull;

public class RouterConfigurator {

    static final int DEFAULT_BODY_LIMIT = 50;
    static final long MB = 1048576L;

    private final Vertx vertx;
    private final JsonObject config;
    private boolean clustered;

    public RouterConfigurator(Vertx vertx, JsonObject config) {
        this.vertx = vertx;
        this.config = config;
    }

    public Router configuredRouter() {
        return makeRouterWithPredefinedHandlers(vertx);
    }

    public Router configuredClusteredRouter() {
        this.clustered = true;
        return makeRouterWithPredefinedHandlers(vertx);
    }

    private Router makeRouterWithPredefinedHandlers(Vertx vertx) {
        Router router = Router.router(vertx);
        addPredefinedHandlers(vertx, router);
        return router;
    }

    private void addPredefinedHandlers(Vertx vertx, Router router) {
        addSessionHandler(vertx, router);
//        addCSRFHandler(router);
        addBodyHandler(router);
    }

    private void addBodyHandler(Router router) {
        Integer bodyLimit = config.getInteger("body.limit");
        router.route().handler(BodyHandler.create().setBodyLimit((isNull(bodyLimit) ? DEFAULT_BODY_LIMIT : bodyLimit) * MB));
    }

    private void addCSRFHandler(Router router) {
        router.route().handler(CSRFHandler.create("You might be under attack!."));
    }

    private void addSessionHandler(Vertx vertx, Router router) {
        SessionStore
                sessionStore = clustered ? ClusteredSessionStore.create(vertx) : LocalSessionStore
                .create(vertx);
        router.route().handler(CookieHandler.create());
        router.route().handler(SessionHandler
                .create(sessionStore)
                .setCookieHttpOnlyFlag(true)
                .setCookieSecureFlag(true)
        );
    }
}
