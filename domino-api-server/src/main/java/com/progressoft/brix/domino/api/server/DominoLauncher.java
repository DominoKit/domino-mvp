package com.progressoft.brix.domino.api.server;

import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static io.vertx.core.impl.FileResolver.DISABLE_FILE_CACHING_PROP_NAME;

public class DominoLauncher extends Launcher {

    protected static final ConfigHolder configHolder = new ConfigHolder();
    protected static final RouterHolder routerHolder = new RouterHolder();


    protected static class ConfigHolder {
        protected JsonObject config;
    }

    protected static class RouterHolder {
        protected Router router;
    }

    public static void main(String[] args) {
        new DominoLauncher().dispatch(args);
    }

    @Override
    public void afterStartingVertx(Vertx vertx) {

        RouterConfigurator routerConfigurator = new RouterConfigurator(vertx, configHolder.config, com.progressoft.brix.domino.api.server
                .SecretKey.generate());
        routerHolder.router =
                PROCESS_ARGS.contains("-cluster") ?
                        routerConfigurator.configuredClusteredRouter() : routerConfigurator.configuredRouter();
    }

    @Override
    public void afterConfigParsed(JsonObject config) {
        configHolder.config = config;
    }


}
