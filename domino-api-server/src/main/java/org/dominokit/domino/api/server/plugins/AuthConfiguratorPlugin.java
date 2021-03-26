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
package org.dominokit.domino.api.server.plugins;

import com.google.auto.service.AutoService;
import org.dominokit.domino.api.server.DominoLoaderPlugin;
import org.dominokit.domino.api.server.PluginContext;
import org.dominokit.domino.api.server.plugins.jwt.JWTHandlersConfigurator;
import org.dominokit.domino.api.server.plugins.jwt.KeycloakJWTOptionsProvider;

@AutoService(DominoLoaderPlugin.class)
public class AuthConfiguratorPlugin extends BaseDominoLoaderPlugin {

  @Override
  public void applyPlugin(CompleteHandler completeHandler) {
    KeycloakJWTOptionsProvider.create(context)
        .load(
            jwtAuthOptions -> {
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
