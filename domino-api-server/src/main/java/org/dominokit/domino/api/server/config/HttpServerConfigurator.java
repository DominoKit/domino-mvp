package org.dominokit.domino.api.server.config;

import org.dominokit.domino.api.server.entrypoint.VertxContext;
import io.vertx.core.http.HttpServerOptions;

@FunctionalInterface
public interface HttpServerConfigurator {
    void configureHttpServer(final VertxContext context, final HttpServerOptions options);
}
