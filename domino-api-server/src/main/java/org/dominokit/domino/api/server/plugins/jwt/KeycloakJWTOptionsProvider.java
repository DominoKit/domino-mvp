/*
 * Copyright © ${year} Dominokit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dominokit.domino.api.server.plugins.jwt;

import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.client.WebClient;
import java.util.function.Consumer;
import org.dominokit.domino.api.server.PluginContext;

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
    webClient
        .getAbs(
            context.getConfig().getString("keycloak.url", KEYCLOAK_URL)
                + context.getConfig().getString("auth.openid.connect.certificates.url", CERTS_URL))
        .send(
            event -> {
              webClient.close();
              JWTAuthOptions jwtAuthOptions =
                  JWTAuthOptionsReader.make(context)
                      .readFromJwksJson(event.result().bodyAsJsonObject());
              jwtAuthOptionsConsumer.accept(jwtAuthOptions);
            });
  }
}
