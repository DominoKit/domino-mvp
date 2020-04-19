package org.dominokit.domino.api.server;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.reactivex.core.http.HttpServer;
import org.dominokit.domino.api.server.entrypoint.VertxContext;

import java.util.function.Consumer;

public class PluginContext {

    public final static int MODULES_LOADER_ORDER = 0;
    public final static int SECURITY_HEADERS_ORDER = 20;
    public final static int AUTH_ORDER = 30;
    public final static int RESTEASY_ORDER = 40;
    public final static int WEBJARS_RESOURCES_HOLDER = 90;
    public final static int STATIC_RESOURCES_ORDER = 100;


    private final int defaultPort;
    private final String httpPortKey;
    private final String webRoot;
    private final Vertx vertx;
    private final Router router;
    private final io.vertx.reactivex.core.Vertx rxVertx;
    private final io.vertx.reactivex.ext.web.Router rxRouter;
    private final JsonObject config;
    private final VertxContext vertxContext;
    private final AsyncResult<HttpServerOptions> options;
    private final Consumer<HttpServer> httpServerConsumer;

    public PluginContext(IsDominoLoader dominoLoader,
                         VertxContext vertxContext, AsyncResult<HttpServerOptions> options, Consumer<HttpServer> httpServerConsumer) {

        this.defaultPort = dominoLoader.getDefaultPort();
        this.httpPortKey = dominoLoader.getHttpPortKey();
        this.webRoot = dominoLoader.getWebroot();
        this.vertx = dominoLoader.getVertx();
        this.router = dominoLoader.getRouter();
        this.rxVertx = dominoLoader.getRxVertx();
        this.rxRouter = dominoLoader.getRxRouter();
        this.config = dominoLoader.getConfig();
        this.vertxContext = vertxContext;
        this.options = options;
        this.httpServerConsumer = httpServerConsumer;
    }

    public int getDefaultPort() {
        return defaultPort;
    }

    public String getHttpPortKey() {
        return httpPortKey;
    }

    public String getWebRoot() {
        return webRoot;
    }

    public Vertx getVertx() {
        return vertx;
    }

    public Router getRouter() {
        return router;
    }

    public io.vertx.reactivex.core.Vertx getRxVertx() {
        return rxVertx;
    }

    public io.vertx.reactivex.ext.web.Router getRxRouter() {
        return rxRouter;
    }

    public JsonObject getConfig() {
        return config;
    }

    public VertxContext getVertxContext() {
        return vertxContext;
    }

    public AsyncResult<HttpServerOptions> getOptions() {
        return options;
    }

    public Consumer<HttpServer> getHttpServerConsumer() {
        return httpServerConsumer;
    }
}
