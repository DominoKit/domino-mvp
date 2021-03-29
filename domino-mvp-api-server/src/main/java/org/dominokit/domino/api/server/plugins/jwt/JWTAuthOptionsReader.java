/*
 * Copyright © 2019 Dominokit
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
    keys.forEach(
        o -> {
          authOptions.addJwk(JsonObject.mapFrom(o));
        });

    authOptions.setPermissionsClaimKey(
        context
            .getConfig()
            .getString("permissions.claims.key", "resource_access/domino-app/roles"));
    return authOptions;
  }
}
