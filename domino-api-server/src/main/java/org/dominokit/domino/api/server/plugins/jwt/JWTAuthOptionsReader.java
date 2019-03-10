package org.dominokit.domino.api.server.plugins.jwt;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuthOptions;

public class JWTAuthOptionsReader {

    public static JWTAuthOptionsReader make() {
        return new JWTAuthOptionsReader();
    }

    public JWTAuthOptions readFromJwksJson(JsonObject jwks) {
        JsonArray keys = jwks.getJsonArray("keys");
        JWTAuthOptions authOptions = new JWTAuthOptions();
        keys.forEach(o -> {
            authOptions.addJwk(JsonObject.mapFrom(o));
        });

        authOptions.setPermissionsClaimKey("resource_access/call-center-app/roles");
        return authOptions;
    }
}
