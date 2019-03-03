package org.dominokit.domino.api.server.config;

import io.vertx.core.http.HttpServerOptions;
import org.dominokit.domino.api.server.entrypoint.VertxContext;

@FunctionalInterface
public interface HttpServerConfigurator {
    void configureHttpServer(final VertxContext context, final HttpServerOptions options);
}
