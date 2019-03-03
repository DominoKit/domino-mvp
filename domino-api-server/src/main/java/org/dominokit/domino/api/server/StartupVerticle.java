package org.dominokit.domino.api.server;

import io.vertx.core.AbstractVerticle;

public class StartupVerticle extends AbstractVerticle {

    @Override
    public void start() {
        new DominoLoader(vertx, DominoLauncher.routerHolder.router, DominoLauncher.configHolder.config).start();
    }

}
