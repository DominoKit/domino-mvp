package com.progressoft.brix.domino.api.server;

import io.vertx.core.AbstractVerticle;

import java.util.logging.Logger;

import static com.progressoft.brix.domino.api.server.DominoLauncher.configHolder;
import static com.progressoft.brix.domino.api.server.DominoLauncher.routerHolder;

public class StartupVerticle extends AbstractVerticle {

    private static final Logger LOGGER = Logger.getLogger(StartupVerticle.class.getName());

    @Override
    public void start() {
        new DominoLoader(vertx, routerHolder.router, configHolder.config).start();
    }

}
