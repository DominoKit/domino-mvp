package org.dominokit.domino.api.server;

import org.dominokit.domino.api.server.config.HttpServerConfigurator;
import org.dominokit.domino.api.server.config.ServerConfigurationLoader;
import org.dominokit.domino.api.server.config.VertxConfiguration;
import org.dominokit.domino.api.server.entrypoint.VertxContext;
import org.dominokit.domino.service.discovery.VertxServiceDiscovery;
import io.vertx.config.ConfigRetriever;
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

import java.nio.file.Paths;
import java.util.ServiceLoader;

import static java.util.Objects.nonNull;

public class DominoLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DominoLoader.class);

    private static final int DEFAULT_PORT = 0;
    private static final String HTTP_PORT_KEY = "http.port";
    private static final int AROUND_6_MONTHS = 15768000;
    private String webroot = "app";

    private final Vertx vertx;
    private final Router router;
    private final JsonObject config;


    public DominoLoader(Vertx vertx, Router router, JsonObject config) {
        this.vertx = vertx;
        this.router = router;
        this.config = config;
        this.webroot = config.getString("webroot", "app");
    }

    public void start() {
        start(event -> {
            if (event.succeeded())
                LOGGER.info("Server started on port : " + event.result().actualPort());
            else
                LOGGER.error("Failed to start server", event.cause());
        });
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

    private VertxContext initializeContext(ImmutableHttpServerOptions immutableHttpServerOptions) {
        return VertxContext.VertxContextBuilder.vertx(vertx)
                .router(router)
                .serverConfiguration(new VertxConfiguration(config))
                .httpServerOptions(immutableHttpServerOptions)
                .vertxServiceDiscovery(new VertxServiceDiscovery(vertx))
                .configRetriever(ConfigRetriever.create(vertx)).build();
    }

    private void onHttpServerConfigurationCompleted(ImmutableHttpServerOptions immutableHttpServerOptions,
                                                    VertxContext vertxContext, AsyncResult<HttpServerOptions> options,
                                                    Handler<AsyncResult<HttpServer>> serverStartupHandler) {
        immutableHttpServerOptions.init(options.result(), options.result().getPort(), options.result().getHost());

        new ServerConfigurationLoader(vertxContext).loadModules();

        initStaticResourceHandler();

        if (options.result().isSsl())
            addSecurityHeadersHandler(router);
        vertx.createHttpServer(options.result()).requestHandler(router::accept)
                .listen(options.result().getPort(), serverStartupHandler);
    }

    private void initStaticResourceHandler() {
        StaticHandler staticHandler = StaticHandler.create();
        if (nonNull(System.getProperty("domino.webroot.location"))) {
            staticHandler.setAllowRootFileSystemAccess(true);
            staticHandler.setWebRoot(systemWebRoot());
        } else {
            staticHandler.setWebRoot(webroot);
        }

        router.route("/").order(Integer.MAX_VALUE - 2)
                .handler(this::serveIndexPage);

        router.route("/static/*").order(Integer.MAX_VALUE - 1)
                .handler(staticHandler)
                .failureHandler(this::serveResource);

        router.route("/*").order(Integer.MAX_VALUE)
                .handler(this::serveResource);

    }

    private void serveResource(RoutingContext context) {
        context.response().sendFile(webroot + context.request().path().replace("/static", ""), event -> {
            if (event.failed())
                serveIndexPage(context);
        });
    }

    private String systemWebRoot() {
        return Paths.get(System.getProperty("domino.webroot.location")).toAbsolutePath().toString();
    }

    private HttpServerResponse serveIndexPage(RoutingContext event) {
        return event.response().putHeader("Content-type", "text/html").sendFile(webroot + "/index.html");
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
                    // IE8+ do not allow opening forRequest attachments in the context forRequest this resource
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