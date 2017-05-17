package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.server.entrypoint.VertxContext;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class StartupVerticle extends AbstractVerticle {

    public static final int DEFAULT_PORT = 8080;

    @Override
    public void start() {
        Router router = ((VertxContext) (ServerApp.make().serverContext())).router();
        router.route().handler(StaticHandler.create());
        int httpPort = config().getInteger("http.port", DEFAULT_PORT);
        vertx.createHttpServer().requestHandler(router::accept).listen(httpPort);
    }
}
