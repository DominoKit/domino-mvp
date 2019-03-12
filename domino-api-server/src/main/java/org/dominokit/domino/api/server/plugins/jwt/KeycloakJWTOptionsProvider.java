package org.dominokit.domino.api.server.plugins.jwt;

import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.client.WebClient;
import org.dominokit.domino.api.server.PluginContext;

import java.util.function.Consumer;

public class KeycloakJWTOptionsProvider {

    public static final String CERTS_URL = "/auth/realms/domino-app/protocol/openid-connect/certs";
    public static final String KEYCLOAK_URL = "http://localhost:9090";
    private final PluginContext context;

    public KeycloakJWTOptionsProvider(PluginContext context) {
        this.context = context;
    }

    public static KeycloakJWTOptionsProvider create(PluginContext context) {
        return new KeycloakJWTOptionsProvider(context);
    }

    public void load(Consumer<JWTAuthOptions> jwtAuthOptionsConsumer) {
        WebClient webClient = WebClient.create(context.getVertx());
        webClient.getAbs(context.getConfig().getString("keycloak.url", KEYCLOAK_URL) + context.getConfig().getString("auth.openid.connect.certificates.url", CERTS_URL))
                .send(event -> {
                    webClient.close();
                    JWTAuthOptions jwtAuthOptions = JWTAuthOptionsReader
                            .make(context)
                            .readFromJwksJson(event.result().bodyAsJsonObject());
                    jwtAuthOptionsConsumer.accept(jwtAuthOptions);
                });
    }
}
