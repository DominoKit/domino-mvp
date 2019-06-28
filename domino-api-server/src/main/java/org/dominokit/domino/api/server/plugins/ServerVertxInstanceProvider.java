package org.dominokit.domino.api.server.plugins;

import com.google.auto.service.AutoService;
import io.vertx.core.Vertx;
import org.dominokit.domino.api.server.ServerApp;
import org.dominokit.domino.api.server.entrypoint.VertxContext;
import org.dominokit.domino.rest.VertxInstanceProvider;

@AutoService(VertxInstanceProvider.class)
public class ServerVertxInstanceProvider implements VertxInstanceProvider {

    private VertxContext getVertxContext() {
        return (VertxContext) ServerApp.make().serverContext();
    }

    @Override
    public Vertx getInstance() {
        return getVertxContext().vertx();
    }

    @Override
    public String getHost() {
        return getVertxContext().httpServerOptions().getHost();
    }

    @Override
    public int getPort() {
        return getVertxContext().httpServerOptions().getPort();
    }

    @Override
    public String getProtocol() {
        return getVertxContext().httpServerOptions().isSsl() ? "https" : "http";
    }
}
