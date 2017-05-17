package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.server.config.HttpServerConfigurator;
import com.progressoft.brix.domino.api.server.entrypoint.ServerContext;
import com.progressoft.brix.domino.api.server.entrypoint.VertxContext;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartupVerticle extends AbstractVerticle {

    private static final Logger LOGGER= Logger.getLogger(StartupVerticle.class.getName());
    public static final int DEFAULT_PORT = 8080;
    public static final String HTTP_PORT_KEY = "http.port";

    @Override
    public void start() {
        try {
            VertxContext vertxContext=ServerApp.make().serverContext().cast(VertxContext.class);
            Router router = vertxContext.router();
            router.route().handler(StaticHandler.create());
            Future<HttpServerOptions> future=Future.future();
            future.setHandler(options -> vertx.createHttpServer(options.result()).requestHandler(router::accept).listen(options.result().getPort()));

            configureHttpServer(vertxContext, future);
        } catch (ServerContext.InvalidContextTypeException e) {
            LOGGER.log(Level.SEVERE, "Not a vertx context!.", e);
            vertx.close();
        }
    }

    private void configureHttpServer(VertxContext vertxContext, Future<HttpServerOptions> future) {
        HttpServerOptions httpServerOptions=new HttpServerOptions();
        httpServerOptions.setPort(vertxContext.config().getInteger(HTTP_PORT_KEY, DEFAULT_PORT));
        ServiceLoader<HttpServerConfigurator> configurators=ServiceLoader.load(HttpServerConfigurator.class);
        configurators.forEach(c -> c.configureHttpServer(vertxContext, httpServerOptions));

        future.complete(httpServerOptions);
    }
}
