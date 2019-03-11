package org.dominokit.domino.api.server;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.reactivex.core.http.HttpServer;
import org.dominokit.domino.api.server.config.HttpServerConfigurator;
import org.dominokit.domino.api.server.config.VertxConfiguration;
import org.dominokit.domino.api.server.entrypoint.VertxContext;
import org.dominokit.domino.service.discovery.VertxServiceDiscovery;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DominoLoader implements IsDominoLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DominoLoader.class);

    public static final int DEFAULT_PORT = 0;
    public static final String HTTP_PORT_KEY = "http.port";

    private String webroot;

    private final Vertx vertx;
    private final Router router;
    private final JsonObject config;
    private final io.vertx.reactivex.core.Vertx rxVertx;
    private final io.vertx.reactivex.ext.web.Router rxRouter;

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

        PluginContext pluginContext = new PluginContext(
                DominoLoader.this,
                vertxContext,
                options,
                httpServerConsumer);

        ServiceLoader<DominoLoaderPlugin> plugins = ServiceLoader.load(DominoLoaderPlugin.class);

        Iterator<DominoLoaderPlugin> iterator = plugins.iterator();
        Iterable<DominoLoaderPlugin> iterable = () -> iterator;


        List<DominoLoaderPlugin> pluginsList = StreamSupport.stream(iterable.spliterator(), false)
                .sorted(Comparator.comparingInt(DominoLoaderPlugin::order))
                .collect(Collectors.toList());

        if (pluginsList.size() > 1) {
            for (int i = 0; i < pluginsList.size() - 1; i++) {
                pluginsList.get(i).init(pluginContext)
                .setNext(pluginsList.get(i + 1));
            }

            pluginsList.get(pluginsList.size()-1).init(pluginContext);
        } else if (pluginsList.size() > 0) {
            pluginsList.get(0).init(pluginContext);
        }


        if (pluginsList.size() > 0) {
            pluginsList.get(0).apply();
        }

//        Iterator<DominoLoaderPlugin> pluginIter = pluginsList.iterator();
//        DominoLoaderPlugin currentPlugin;
//        if (pluginIter.hasNext()) {
//            currentPlugin = pluginIter.next();
//            while (pluginIter.hasNext()) {
//                DominoLoaderPlugin plugin = pluginIter.next();
//                currentPlugin.init(pluginContext, plugin);
//                currentPlugin = plugin;
//            }
//        }


//        pluginIter
//                .map(plugin -> plugin.init())
//                .filter(DominoLoaderPlugin::isEnabled)
//                .forEach(DominoLoaderPlugin::apply);


    }



    private void configureHttpServer(VertxContext vertxContext, Future<HttpServerOptions> future) {
        HttpServerOptions httpServerOptions = new HttpServerOptions();
        httpServerOptions.setPort(vertxContext.config().getInteger(HTTP_PORT_KEY, DEFAULT_PORT));
        ServiceLoader<HttpServerConfigurator> configurators = ServiceLoader.load(HttpServerConfigurator.class);
        configurators.forEach(c -> c.configureHttpServer(vertxContext, httpServerOptions));
        httpServerOptions.setCompressionSupported(true);

        future.complete(httpServerOptions);
    }

    @Override
    public int getDefaultPort() {
        return DEFAULT_PORT;
    }

    @Override
    public String getHttpPortKey() {
        return HTTP_PORT_KEY;
    }

    @Override
    public String getWebroot() {
        return webroot;
    }

    @Override
    public Vertx getVertx() {
        return vertx;
    }

    @Override
    public Router getRouter() {
        return router;
    }

    @Override
    public JsonObject getConfig() {
        return config;
    }

    @Override
    public io.vertx.reactivex.core.Vertx getRxVertx() {
        return rxVertx;
    }

    @Override
    public io.vertx.reactivex.ext.web.Router getRxRouter() {
        return rxRouter;
    }

}