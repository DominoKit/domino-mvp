package org.dominokit.domino.api.server;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import org.dominokit.domino.api.server.logging.DefaultRemoteLogger;
import org.dominokit.domino.api.server.logging.RemoteLogger;
import org.dominokit.domino.api.server.logging.RemoteLoggingHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
public class RouterConfigurator {

    private static final int DEFAULT_BODY_LIMIT = 50;
    private static final long MB = 1048576L;
    private static final String REMOTE_LOGGER = "remote.logger";

    private final Vertx vertx;
    private final JsonObject config;
    private final String secret;
    private boolean clustered;

    public RouterConfigurator(Vertx vertx, JsonObject config, String secret) {
        this.vertx = vertx;
        this.config = config;
        this.secret = secret;
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
        addCorsHandler(router);
        addSessionHandler(vertx, router);
        addCSRFHandler(router);
//        addRemoteExceptionHandler(router);
    }

    private void addCorsHandler(Router router) {
        if (config.getBoolean("cors.enabled", true)) {
            router.route().handler(CorsHandler.create("*")
                    .allowedHeaders(new HashSet<>(Arrays.asList("Content-Type", "X-HTTP-Method-Override", "X-XSRF-TOKEN")))
                    .allowedMethods(Stream.of(HttpMethod.values()).collect(Collectors.toSet())));
        }
    }

    private void addRemoteExceptionHandler(Router router) {
        if (config.getBoolean("remove.exception.handler.enabled", true)) {
            RemoteLogger remoteLogger = StreamSupport.stream(ServiceLoader.load(RemoteLogger.class).spliterator(), false)
                    .filter(logger -> logger.getClass().getName().equals(config.getString(REMOTE_LOGGER)))
                    .findFirst().orElseGet(DefaultRemoteLogger::new);
            router.route("/logging/remoteLogging")
                    .handler(new RemoteLoggingHandler(remoteLogger));
        }
    }

    private void addCSRFHandler(Router router) {
        if (config.getBoolean("csrf.enabled", true)) {
            router.route().handler(new DominoCSRFHandler(secret, config));
        }
    }

    private void addSessionHandler(Vertx vertx, Router router) {
        if (config.getBoolean("session.enabled", true)) {
            SessionStore
                    sessionStore = clustered ? ClusteredSessionStore.create(vertx) : LocalSessionStore
                    .create(vertx);
            if (config.getBoolean("ssl.enabled", false)) {
                router.route().handler(SessionHandler
                        .create(sessionStore)
                        .setCookieHttpOnlyFlag(true)
                        .setCookieSecureFlag(true));
            } else {
                router.route().handler(SessionHandler
                        .create(sessionStore));
            }
        }
    }
}
