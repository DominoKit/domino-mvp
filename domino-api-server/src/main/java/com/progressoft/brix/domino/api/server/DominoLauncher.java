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

    static final long MB = 1048576L;

    private static class ConfigHolder{
        private JsonObject config;
    }

    private static final ConfigHolder configHolder=new ConfigHolder();

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
        addSecurityHeadersHandler(router);
        addCSRFHandler(router);
        addBodyHandler(router);
    }

    private void addBodyHandler(Router router) {
        router.route().handler(BodyHandler.create().setBodyLimit(50 * MB));
    }

    private void addCSRFHandler(Router router) {
        router.route().handler(CSRFHandler.create("You might be under attack!."));
    }

    private void addSecurityHeadersHandler(Router router) {
        router.route().handler(ctx -> {
            ctx.response()
                    // do not allow proxies to cache the data
                    .putHeader("Cache-Control", "no-store, no-cache")
                    // prevents Internet Explorer from MIME - sniffing a
                    // response away from the declared content-type
                    .putHeader("X-Content-Type-Options", "nosniff")
                    // Strict HTTPS (for about ~6Months)
                    .putHeader("Strict-Transport-Security", "max-age=" + 15768000)
                    // IE8+ do not allow opening of attachments in the context of this resource
                    .putHeader("X-Download-Options", "noopen")
                    // enable XSS for IE
                    .putHeader("X-XSS-Protection", "1; mode=block")
                    // deny frames
                    .putHeader("X-FRAME-OPTIONS", "DENY");
            ctx.next();
        });
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
