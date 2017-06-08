package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.server.config.ServerConfigurationLoader;
import com.progressoft.brix.domino.api.server.config.VertxConfiguration;
import com.progressoft.brix.domino.api.server.entrypoint.VertxContext;
import io.vertx.core.Launcher;
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

public class DominoLauncher extends Launcher {

    private static final ConfigHolder configHolder=new ConfigHolder();
    static final long MB = 1048576L;

    private static class ConfigHolder{
        private JsonObject config;
    }

    public static void main(String[] args) {
        new DominoLauncher().dispatch(args);
    }

    @Override
    public void afterStartingVertx(Vertx vertx) {

        System.setProperty("vertx.disableFileCaching", "true");
        Router router = makeRouterWithPredefinedHandlers(vertx);

        VertxContext vertxContext =
                new VertxContext(vertx, router, new VertxConfiguration(configHolder.config));
        new ServerConfigurationLoader(vertxContext).loadModules();
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
                sessionStore=PROCESS_ARGS.contains("-cluster")? ClusteredSessionStore.create(vertx): LocalSessionStore.create(vertx);
        router.route().handler(CookieHandler.create());
        router.route().handler(SessionHandler
                .create(sessionStore)
                .setCookieHttpOnlyFlag(true)
                .setCookieSecureFlag(true)
        );
    }

    @Override
    public void afterConfigParsed(JsonObject config) {
        configHolder.config=config;
    }
}
