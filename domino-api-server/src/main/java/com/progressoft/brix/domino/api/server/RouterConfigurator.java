package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.server.logging.DefaultRemoteLogger;
import com.progressoft.brix.domino.api.server.logging.RemoteLogger;
import com.progressoft.brix.domino.api.server.logging.RemoteLoggingHandler;
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

import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

import static java.util.Objects.isNull;

public class RouterConfigurator {

    private static final int DEFAULT_BODY_LIMIT = 50;
    private static final long MB = 1048576L;
    private static final String REMOTE_LOGGER = "remote.logger";

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
        // TODO add a confiuration to turn this filter on/off.
//        addCSRFHandler(router);
        addBodyHandler(router);
        addRemoteExceptionHandler(router);
    }

    private void addRemoteExceptionHandler(Router router) {
        RemoteLogger remoteLogger = StreamSupport.stream(ServiceLoader.load(RemoteLogger.class).spliterator(), false)
                .filter(logger -> logger.getClass().getName().equals(config.getString(REMOTE_LOGGER)))
                .findFirst().orElseGet(DefaultRemoteLogger::new);
        router.route("/service/remoteLogging")
                .handler(new RemoteLoggingHandler(remoteLogger));
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
