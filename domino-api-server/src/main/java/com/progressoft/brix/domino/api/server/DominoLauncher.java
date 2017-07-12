package com.progressoft.brix.domino.api.server;

import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class DominoLauncher extends Launcher {

    static final ConfigHolder configHolder = new ConfigHolder();
    static final RouterHolder routerHolder = new RouterHolder();


    protected static class ConfigHolder {
        JsonObject config;
    }

    protected static class RouterHolder {
        Router router;
    }

    public static void main(String[] args) {
        new DominoLauncher().dispatch(args);
    }

    @Override
    public void afterStartingVertx(Vertx vertx) {
        System.setProperty("vertx.disableFileCaching", "true");
        RouterConfigurator routerConfigurator=new RouterConfigurator(vertx, configHolder.config);
        routerHolder.router= PROCESS_ARGS.contains("-cluster")?routerConfigurator.configuredClusteredRouter():routerConfigurator.configuredRouter();
    }

    @Override
    public void afterConfigParsed(JsonObject config) {
        configHolder.config = config;
    }
}
