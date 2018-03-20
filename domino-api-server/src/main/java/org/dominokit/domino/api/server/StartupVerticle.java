package org.dominokit.domino.api.server;

import io.vertx.core.AbstractVerticle;

import static org.dominokit.domino.api.server.DominoLauncher.configHolder;
import static org.dominokit.domino.api.server.DominoLauncher.routerHolder;

public class StartupVerticle extends AbstractVerticle {

    @Override
    public void start() {
        new DominoLoader(vertx, DominoLauncher.routerHolder.router, DominoLauncher.configHolder.config).start();
    }

}
