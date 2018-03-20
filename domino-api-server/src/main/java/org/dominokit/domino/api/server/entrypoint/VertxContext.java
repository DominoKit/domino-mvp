package org.dominokit.domino.api.server.entrypoint;

import org.dominokit.domino.api.server.config.ServerConfiguration;
import org.dominokit.domino.service.discovery.VertxServiceDiscovery;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.function.Supplier;

public class VertxContext implements ServerContext {

    private final Router router;
    private final ServerConfiguration config;
    private final Vertx vertx;
    private final VertxServiceDiscovery serviceDiscovery;
    private final DominoHttpServerOptions httpServerOptions;
    private final ConfigRetriever configRetriever;

    private VertxContext(Vertx vertx, Router router, ServerConfiguration config, VertxServiceDiscovery serviceDiscovery,
                         DominoHttpServerOptions httpServerOptions, ConfigRetriever configRetriever) {
        this.router = router;
        this.config = config;
        this.vertx = vertx;
        this.serviceDiscovery = serviceDiscovery;
        this.httpServerOptions = httpServerOptions;
        this.configRetriever = configRetriever;
    }

    @Override
    public ServerConfiguration config() {
        return config;
    }

    @Override
    public void publishService(String path, Supplier<?> factory) {
        publishEndPoint("/service/" + path, factory);
    }

    @Override
    public void publishEndPoint(String path, Supplier<?> factory) {
        router.route().path(path).handler((Handler<RoutingContext>) factory.get());
    }

    public Router router() {
        return this.router;
    }

    public Vertx vertx() {
        return this.vertx;
    }

    public VertxServiceDiscovery serviceDiscovery() {
        return this.serviceDiscovery;
    }

    public DominoHttpServerOptions httpServerOptions() {
        return this.httpServerOptions;
    }

    public ConfigRetriever configRetriever() {
        return this.configRetriever;
    }

    public static class VertxContextBuilder {
        private Router router;
        private ServerConfiguration config;
        private Vertx vertx;
        private VertxServiceDiscovery serviceDiscovery;
        private DominoHttpServerOptions httpServerOptions;
        private ConfigRetriever configRetriever;


        private VertxContextBuilder(Vertx vertx) {
            this.vertx = vertx;
        }

        public VertxContextBuilder router(Router router) {
            this.router = router;
            return this;
        }

        public VertxContextBuilder serverConfiguration(ServerConfiguration config) {
            this.config = config;
            return this;
        }

        public static VertxContextBuilder vertx(Vertx vertx) {
            return new VertxContextBuilder(vertx);
        }

        public VertxContextBuilder vertxServiceDiscovery(VertxServiceDiscovery serviceDiscovery) {
            this.serviceDiscovery = serviceDiscovery;
            return this;
        }

        public VertxContextBuilder httpServerOptions(DominoHttpServerOptions DominoHttpServerOptions) {
            this.httpServerOptions = DominoHttpServerOptions;
            return this;
        }

        public VertxContextBuilder configRetriever(ConfigRetriever configRetriever) {
            this.configRetriever = configRetriever;
            return this;
        }

        public VertxContext build() {
            return new VertxContext(vertx, router, config, serviceDiscovery, httpServerOptions, configRetriever);
        }
    }
}
