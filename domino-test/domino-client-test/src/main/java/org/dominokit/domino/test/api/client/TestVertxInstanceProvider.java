package org.dominokit.domino.test.api.client;

import com.google.auto.service.AutoService;
import io.vertx.core.Vertx;
import org.dominokit.domino.api.server.ServerApp;
import org.dominokit.domino.api.server.entrypoint.VertxContext;
import org.dominokit.rest.server.VertxInstanceProvider;

@AutoService(VertxInstanceProvider.class)
public class TestVertxInstanceProvider implements VertxInstanceProvider {

    @Override
    public Vertx getInstance() {
        return ((VertxContext) ServerApp.make().serverContext()).vertx();
    }
}
