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

import io.vertx.core.json.JsonArray;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.handler.JWTAuthHandler;
import java.util.Collections;
import org.dominokit.domino.api.server.PluginContext;

public class JWTHandlersConfigurator {

  private final PluginContext context;
  private final JWTAuth jwtAuth;

  private JWTHandlersConfigurator(PluginContext context, JWTAuthOptions jwtAuthOptions) {
    this.context = context;
    jwtAuth = JWTAuth.create(context.getVertx(), jwtAuthOptions);
  }

  public static JWTHandlersConfigurator create(
      PluginContext context, JWTAuthOptions jwtAuthOptions) {
    return new JWTHandlersConfigurator(context, jwtAuthOptions);
  }

  public void configure() {
    JWTAuthHandler jwtAuthHandler = JWTAuthHandler.create(jwtAuth);
    JsonArray protectedPathsJson =
        context
            .getConfig()
            .getJsonArray(
                "jwt.protected.resources", new JsonArray(Collections.singletonList("/service/*")));
    protectedPathsJson
        .iterator()
        .forEachRemaining(
            resourcePath ->
                context.getRouter().route(resourcePath.toString()).handler(jwtAuthHandler));
  }
}
