package com.progressoft.brix.domino.api.server;

import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class DominoLauncher extends Launcher {

    private static class ConfigHolder{
        private JsonObject config;
    }

    private static final ConfigHolder configHolder=new ConfigHolder();

    public static void main(String[] args) {
        new DominoLauncher().dispatch(args);
    }

    @Override
    public void afterStartingVertx(Vertx vertx) {
        System.setProperty("vertx.disableFileCaching", "true");
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        VertxContext vertxContext =
                new VertxContext(vertx, router, new VertxConfiguration(configHolder.config));
        new ServerConfigurationLoader(vertxContext).loadModules();
    }

    @Override
    public void afterConfigParsed(JsonObject config) {
        configHolder.config=config;
    }
}
