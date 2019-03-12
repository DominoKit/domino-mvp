package org.dominokit.domino.api.server.plugins;

import com.google.auto.service.AutoService;
import org.dominokit.domino.api.server.DominoLoaderPlugin;
import org.dominokit.domino.api.server.PluginContext;
import org.dominokit.domino.api.server.plugins.jwt.JWTHandlersConfigurator;
import org.dominokit.domino.api.server.plugins.jwt.KeycloakJWTOptionsProvider;

@AutoService(DominoLoaderPlugin.class)
public class AuthConfigratorPlugin extends BaseDominoLoaderPlugin {

    @Override
    public void applyPlugin(CompleteHandler completeHandler) {
        KeycloakJWTOptionsProvider.create(context)
                .load(jwtAuthOptions -> {
                    JWTHandlersConfigurator.create(context, jwtAuthOptions).configure();
                    completeHandler.onCompleted();
                });
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
