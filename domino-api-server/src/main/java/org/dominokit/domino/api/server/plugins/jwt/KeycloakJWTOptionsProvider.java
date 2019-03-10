package org.dominokit.domino.api.server.plugins.jwt;

import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.client.WebClient;
import org.dominokit.domino.api.server.entrypoint.VertxContext;

import java.util.function.Consumer;

public class KeycloakJWTOptionsProvider {

    public static final String CERTS_URL = "/auth/realms/call-center/protocol/openid-connect/certs";
    private final VertxContext vertxContext;

    public KeycloakJWTOptionsProvider(VertxContext vertxContext) {
        this.vertxContext = vertxContext;
    }

    public static KeycloakJWTOptionsProvider create(VertxContext vertxContext) {
        return new KeycloakJWTOptionsProvider(vertxContext);
    }

    public void load(Consumer<JWTAuthOptions> jwtAuthOptionsConsumer) {
        WebClient webClient = WebClient.create(vertxContext.vertx());
        webClient.getAbs(vertxContext.config().getString("keycloak.url") + CERTS_URL)
                .send(event -> {
                    webClient.close();
                    JWTAuthOptions jwtAuthOptions = JWTAuthOptionsReader
                            .make()
                            .readFromJwksJson(event.result().bodyAsJsonObject());
                    jwtAuthOptionsConsumer.accept(jwtAuthOptions);
                });
    }
}
