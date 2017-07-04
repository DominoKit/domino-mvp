package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.server.config.HttpServerConfigurator;
import com.progressoft.brix.domino.api.server.config.ServerConfigurationLoader;
import com.progressoft.brix.domino.api.server.config.VertxConfiguration;
import com.progressoft.brix.domino.api.server.entrypoint.VertxContext;
import com.progressoft.brix.domino.service.discovery.VertxServiceDiscovery;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.ServiceLoader;

public class DominoLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DominoLoader.class);

    public static final int DEFAULT_PORT = 8080;
    public static final String HTTP_PORT_KEY = "http.port";
    public static final int AROUND_6_MONTHS = 15768000;

    private final Vertx vertx;
    private final Router router;
    private final JsonObject config;


    public DominoLoader(Vertx vertx, Router router, JsonObject config) {
        this.vertx = vertx;
        this.router = router;
        this.config = config;
    }

    public void start(Handler<AsyncResult<HttpServer>> serverStartupHandler) {
        ImmutableHttpServerOptions immutableHttpServerOptions = new ImmutableHttpServerOptions();
        VertxContext vertxContext = initializeContext(immutableHttpServerOptions);

        Future<HttpServerOptions> future = Future.future();
        future.setHandler(
                options -> onHttpServerConfigurationCompleted(immutableHttpServerOptions, vertxContext, options,
                        serverStartupHandler));

        configureHttpServer(vertxContext, future);
    }

    public void start() {
        start(event -> {
            if (event.succeeded())
                LOGGER.info("Server started on port : " + event.result().actualPort());
            else
                LOGGER.error("Failed to start server", event.cause());
        });
    }

    private VertxContext initializeContext(ImmutableHttpServerOptions immutableHttpServerOptions) {
        return VertxContext.VertxContextBuilder.vertx(vertx)
                .router(router)
                .serverConfiguration(new VertxConfiguration(config))
                .httpServerOptions(immutableHttpServerOptions)
                .vertxServiceDiscovery(new VertxServiceDiscovery(vertx)).build();
    }

    private void onHttpServerConfigurationCompleted(ImmutableHttpServerOptions immutableHttpServerOptions,
                                                    VertxContext vertxContext, AsyncResult<HttpServerOptions> options,
                                                    Handler<AsyncResult<HttpServer>> serverStartupHandler) {
        immutableHttpServerOptions.init(options.result(), options.result().getPort(), options.result().getHost());
        new ServerConfigurationLoader(vertxContext).loadModules();

        router.route("/static/*").handler(StaticHandler.create())
                .failureHandler(this::serveIndexPage);

        router.route("/*").handler(this::serveIndexPage);

        if (options.result().isSsl())
            addSecurityHeadersHandler(router);
        vertx.createHttpServer(options.result()).requestHandler(router::accept)
                .listen(options.result().getPort(), serverStartupHandler);
    }

    private HttpServerResponse serveIndexPage(RoutingContext event) {
        return event.response().putHeader("Content-type", "text/html").sendFile("webroot/index.html");
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
                    .putHeader("Strict-Transport-Security", "max-age=" + AROUND_6_MONTHS)
                    // IE8+ do not allow opening of attachments in the context of this resource
                    .putHeader("X-Download-Options", "noopen")
                    // enable XSS for IE
                    .putHeader("X-XSS-Protection", "1; mode=block")
                    // deny frames
                    .putHeader("X-FRAME-OPTIONS", "DENY");
            ctx.next();
        });
    }

    private void configureHttpServer(VertxContext vertxContext, Future<HttpServerOptions> future) {
        HttpServerOptions httpServerOptions = new HttpServerOptions();
        httpServerOptions.setPort(vertxContext.config().getInteger(HTTP_PORT_KEY, DEFAULT_PORT));
        ServiceLoader<HttpServerConfigurator> configurators = ServiceLoader.load(HttpServerConfigurator.class);
        configurators.forEach(c -> c.configureHttpServer(vertxContext, httpServerOptions));
        httpServerOptions.setCompressionSupported(true);

        future.complete(httpServerOptions);
    }
}
