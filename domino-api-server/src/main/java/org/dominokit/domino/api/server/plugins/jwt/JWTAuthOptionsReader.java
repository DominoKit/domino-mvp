package org.dominokit.domino.api.server.plugins.jwt;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import org.dominokit.domino.api.server.PluginContext;

public class JWTAuthOptionsReader {

    private final PluginContext context;

    public JWTAuthOptionsReader(PluginContext context) {
        this.context = context;
    }

    public static JWTAuthOptionsReader make(PluginContext context) {
        return new JWTAuthOptionsReader(context);
    }

    public JWTAuthOptions readFromJwksJson(JsonObject jwks) {
        JsonArray keys = jwks.getJsonArray("keys");
        JWTAuthOptions authOptions = new JWTAuthOptions();
        keys.forEach(o -> {
            authOptions.addJwk(JsonObject.mapFrom(o));
        });

        authOptions.setPermissionsClaimKey(context.getConfig().getString("permissions.claims.key", "resource_access/domino-app/roles"));
        return authOptions;
    }
}
