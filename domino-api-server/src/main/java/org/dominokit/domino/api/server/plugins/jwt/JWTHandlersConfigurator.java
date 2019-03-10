package org.dominokit.domino.api.server.plugins.jwt;

import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.handler.JWTAuthHandler;
import org.dominokit.domino.api.server.entrypoint.VertxContext;

public class JWTHandlersConfigurator {

    private VertxContext vertxContext;
    private final JWTAuth jwtAuth;

    private JWTHandlersConfigurator(VertxContext vertxContext, JWTAuthOptions jwtAuthOptions) {
        this.vertxContext = vertxContext;
        jwtAuth = JWTAuth.create(vertxContext.vertx(), jwtAuthOptions);
    }

    public static JWTHandlersConfigurator create(VertxContext vertxContext, JWTAuthOptions jwtAuthOptions) {
        return new JWTHandlersConfigurator(vertxContext, jwtAuthOptions);
    }

    public void configure() {
        JWTAuthHandler jwtAuthHandler = JWTAuthHandler.create(jwtAuth);
        vertxContext.router().route("/service/*")
                .handler(jwtAuthHandler);
    }
}
