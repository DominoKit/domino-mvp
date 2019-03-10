package org.dominokit.domino.api.server.plugins;

import com.google.auto.service.AutoService;
import org.dominokit.domino.api.server.DominoLoaderPlugin;
import org.dominokit.domino.api.server.PluginContext;
import org.dominokit.domino.api.server.plugins.jwt.JWTHandlersConfigurator;
import org.dominokit.domino.api.server.plugins.jwt.KeycloakJWTOptionsProvider;

@AutoService(DominoLoaderPlugin.class)
public class AuthConfigratorPlugin implements DominoLoaderPlugin {

    private PluginContext context;

    @Override
    public DominoLoaderPlugin init(PluginContext context) {
        this.context = context;
        return this;
    }

    @Override
    public void apply() {
        KeycloakJWTOptionsProvider.create(context.getVertxContext())
                .load(jwtAuthOptions -> JWTHandlersConfigurator.create(context.getVertxContext(), jwtAuthOptions).configure());
    }

    @Override
    public int order() {
        return PluginContext.AUTH_ORDER;
    }

    @Override
    public boolean isEnabled() {
        return context.getConfig().getBoolean("jwt.auth.handler.enabled", false);
    }
}
