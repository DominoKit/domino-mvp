package org.dominokit.domino.api.server.plugins.jwt;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.handler.JWTAuthHandler;
import org.dominokit.domino.api.server.PluginContext;

import java.util.Collections;

public class JWTHandlersConfigurator {

    private final PluginContext context;
    private final JWTAuth jwtAuth;

    private JWTHandlersConfigurator(PluginContext context, JWTAuthOptions jwtAuthOptions) {
        this.context = context;
        jwtAuth = JWTAuth.create(context.getVertx(), jwtAuthOptions);
    }

    public static JWTHandlersConfigurator create(PluginContext context, JWTAuthOptions jwtAuthOptions) {
        return new JWTHandlersConfigurator(context, jwtAuthOptions);
    }

    public void configure() {
        JWTAuthHandler jwtAuthHandler = JWTAuthHandler.create(jwtAuth);
        JsonArray protectedPathsJson = context.getConfig().getJsonArray("jwt.protected.resources", new JsonArray(Collections.singletonList("/service/*")));
        protectedPathsJson.iterator()
                .forEachRemaining(resourcePath -> context.getRouter().route(resourcePath.toString())
                        .handler(jwtAuthHandler));
    }
}
