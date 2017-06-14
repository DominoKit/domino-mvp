package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.server.config.ServerConfigurationLoader;
import com.progressoft.brix.domino.api.server.config.VertxConfiguration;
import com.progressoft.brix.domino.api.server.entrypoint.VertxContext;
import com.progressoft.brix.domino.service.discovery.VertxServiceDiscovery;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;

public class DominoLauncher extends Launcher {

    static final ConfigHolder configHolder = new ConfigHolder();
    static final RouterHolder routerHolder = new RouterHolder();
    static final long MB = 1048576L;

    protected static class ConfigHolder {
        JsonObject config;
    }

    protected static class RouterHolder {
        Router router;
    }

    public static void main(String[] args) {
        new DominoLauncher().dispatch(args);
    }

    @Override
    public void afterStartingVertx(Vertx vertx) {
        System.setProperty("vertx.disableFileCaching", "true");
        routerHolder.router=makeRouterWithPredefinedHandlers(vertx);

    }

    private Router makeRouterWithPredefinedHandlers(Vertx vertx) {
        Router router = Router.router(vertx);
        addPredefinedHandlers(vertx, router);
        return router;
    }

    private void addPredefinedHandlers(Vertx vertx, Router router) {
        addSessionHandler(vertx, router);
        addCSRFHandler(router);
        addBodyHandler(router);
    }

    private void addBodyHandler(Router router) {
        router.route().handler(BodyHandler.create().setBodyLimit(50 * MB));
    }

    private void addCSRFHandler(Router router) {
        router.route().handler(CSRFHandler.create("You might be under attack!."));
    }

    private void addSessionHandler(Vertx vertx, Router router) {
        SessionStore
                sessionStore = PROCESS_ARGS.contains("-cluster") ? ClusteredSessionStore.create(vertx) : LocalSessionStore.create(vertx);
        router.route().handler(CookieHandler.create());
        router.route().handler(SessionHandler
                .create(sessionStore)
                .setCookieHttpOnlyFlag(true)
                .setCookieSecureFlag(true)
        );
    }

    @Override
    public void afterConfigParsed(JsonObject config) {
        configHolder.config = config;
    }
}
