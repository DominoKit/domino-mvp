package org.dominokit.domino.api.server;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.reactivex.core.http.HttpServer;
import org.dominokit.domino.api.server.config.HttpServerConfigurator;
import org.dominokit.domino.api.server.config.ServerConfigurationLoader;
import org.dominokit.domino.api.server.config.VertxConfiguration;
import org.dominokit.domino.api.server.entrypoint.VertxContext;
import org.dominokit.domino.api.server.resteasy.VertxPluginRequestHandler;
import org.dominokit.domino.service.discovery.VertxServiceDiscovery;
import org.jboss.resteasy.plugins.server.vertx.VertxResteasyDeployment;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.ws.rs.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;

public class DominoLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DominoLoader.class);

    private static final int DEFAULT_PORT = 0;
    private static final String HTTP_PORT_KEY = "http.port";
    private static final int AROUND_6_MONTHS = 15768000;
    private String webroot;

    private final Vertx vertx;
    private final Router router;
    private final JsonObject config;
    private io.vertx.reactivex.core.Vertx rxVertx;
    private io.vertx.reactivex.ext.web.Router rxRouter;


    public DominoLoader(Vertx vertx, Router router, JsonObject config) {
        this.vertx = vertx;
        this.rxVertx = new io.vertx.reactivex.core.Vertx(vertx);
        this.router = router;
        this.rxRouter = new io.vertx.reactivex.ext.web.Router(router);
        this.config = config;
        this.webroot = config.getString("webroot", "app");

        AppGlobals app = AppGlobals.init();
        app.setConfig(config);
        app.setRouter(rxRouter);
        app.setVertx(rxVertx);
    }

    public void start() {
        start(httpServer -> {
        });
    }

    public void start(Consumer<HttpServer> httpServerConsumer) {
        ImmutableHttpServerOptions immutableHttpServerOptions = new ImmutableHttpServerOptions();
        VertxContext vertxContext = initializeContext(immutableHttpServerOptions);

        Future<HttpServerOptions> future = Future.future();
        future.setHandler(
                options -> onHttpServerConfigurationCompleted(immutableHttpServerOptions, vertxContext, options, httpServerConsumer));

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
                                                    Consumer<HttpServer> httpServerConsumer) {
        immutableHttpServerOptions.init(options.result(), options.result().getPort(), options.result().getHost());

        new ServerConfigurationLoader(vertxContext).loadModules();

        List<Class<?>> resources = ServerApp.make().resourcesRepository().getResources();
        resources.add(AppResource.class);
        Class<?>[] resourceClasses = new Class[resources.size()];

        resources.toArray(resourceClasses);

        initStaticResourceHandler();

        if (options.result().isSsl()) {
            addSecurityHeadersHandler(router);
        }

        VertxResteasyDeployment vertxResteasyDeployment = setupResteasy(resourceClasses);

        AppGlobals globals = AppGlobals.get();
        globals.setDeployment(vertxResteasyDeployment);

        startVertx(vertxResteasyDeployment, vertxContext);

        rxVertx.createHttpServer(options.result())
                .requestHandler(rxRouter)
                .rxListen(options.result().getPort())
                .doOnSuccess(httpServer -> {
                    LOGGER.info("Server started on port : " + httpServer.actualPort());
                    httpServerConsumer.accept(httpServer);
                })
                .doOnError(throwable -> LOGGER.error("Failed to start server", throwable))
                .subscribe();
    }

    protected VertxResteasyDeployment setupResteasy(Class<?>... resourceOrProviderClasses) {
        VertxResteasyDeployment deployment = new VertxResteasyDeployment();
        deployment.getDefaultContextObjects().put(io.vertx.reactivex.core.Vertx.class, rxVertx);

        for (Class<?> klass : resourceOrProviderClasses) {
            if (klass.isAnnotationPresent(Path.class)) {
                deployment.getActualResourceClasses().add(klass);
            }
        }

        deployment.start();

        return deployment;
    }

    private void startVertx(VertxResteasyDeployment deployment, VertxContext vertxContext) {

        rxVertx = new io.vertx.reactivex.core.Vertx(vertx);
        VertxPluginRequestHandler resteasyHandler = new VertxPluginRequestHandler(rxVertx, deployment, new ArrayList<>());

        rxRouter.route(config.getString("resource.root.path", "") + "/*")
                .order(Integer.MAX_VALUE - 1)
                .handler(routingContext -> {
                    ResteasyProviderFactory.pushContext(ResourceContext.class, new ResourceContext(rxVertx, rxRouter, vertxContext, routingContext));
                    try {
                        resteasyHandler.handle(routingContext.request());
                    } catch (Exception ex) {
                        serveResource(routingContext.getDelegate());
                    }
                })
                .failureHandler(context -> {
                    serveResource(context.getDelegate());
                });


    }

    private void initStaticResourceHandler() {
        StaticHandler staticHandler = StaticHandler.create();
        if (nonNull(System.getProperty("domino.webroot.location"))) {
            staticHandler.setAllowRootFileSystemAccess(true);
            staticHandler.setWebRoot(systemWebRoot());
        } else {
            staticHandler.setWebRoot(webroot);
        }

        router.route("/").order(Integer.MAX_VALUE - 3)
                .handler(this::serveIndexPage);

        router.route("/static/*").order(Integer.MAX_VALUE - 2)
                .handler(staticHandler)
                .failureHandler(this::serveResource);

        router.route("/*").order(Integer.MAX_VALUE)
                .handler(this::serveResource);

    }

    private void serveResource(RoutingContext context) {
        context.response()
                .sendFile(webroot + context.request().path().replace("/static", ""), event -> {
                    if (event.failed())
                        serveIndexPage(context);
                });
    }

    private String systemWebRoot() {
        return Paths.get(System.getProperty("domino.webroot.location")).toAbsolutePath().toString();
    }

    private HttpServerResponse serveIndexPage(RoutingContext event) {
        return event.response().putHeader("Content-type", "text/html")
                .sendFile(webroot + "/index.html");
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