package org.dominokit.domino.api.server;

import org.dominokit.domino.api.server.logging.DefaultRemoteLogger;
import org.dominokit.domino.api.server.logging.RemoteLogger;
import org.dominokit.domino.api.server.logging.RemoteLoggingHandler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;

import java.util.Arrays;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.isNull;

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
        addBodyHandler(router);
        addSessionHandler(vertx, router);
        addCSRFHandler(router);
        addRemoteExceptionHandler(router);
    }

    private void addCorsHandler(Router router) {
        router.route().handler(CorsHandler.create("*")
                .allowedHeaders(new HashSet<>(Arrays.asList("Content-Type", "X-HTTP-Method-Override", "X-XSRF-TOKEN")))
                .allowedMethods(Stream.of(HttpMethod.values()).collect(Collectors.toSet())));
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
        JsonArray jsonArray = config.getJsonArray("csrf.whitelist", new JsonArray());
        Set<String> whiteList=new HashSet<>();
        jsonArray.forEach(o-> whiteList.add(o.toString()));

        router.route().handler(new DominoCSRFHandler(secret,config));
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
