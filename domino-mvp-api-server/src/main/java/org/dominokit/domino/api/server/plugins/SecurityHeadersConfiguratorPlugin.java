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
package org.dominokit.domino.api.server.plugins;

import com.google.auto.service.AutoService;
import io.vertx.ext.web.Router;
import org.dominokit.domino.api.server.DominoLoaderPlugin;
import org.dominokit.domino.api.server.PluginContext;

@AutoService(DominoLoaderPlugin.class)
public class SecurityHeadersConfiguratorPlugin extends BaseDominoLoaderPlugin {

  private static final int AROUND_6_MONTHS = 15768000;

  @Override
  public void applyPlugin(CompleteHandler completeHandler) {
    addSecurityHeadersHandler(context.getRouter());
    completeHandler.onCompleted();
  }

  private void addSecurityHeadersHandler(Router router) {
    router
        .route()
        .handler(
            ctx -> {
              ctx.response()
                  // do not allow proxies to cache the data
                  .putHeader("Cache-Control", "no-store, no-cache")
                  // prevents Internet Explorer from MIME - sniffing a
                  // response away from the declared content-type
                  .putHeader("X-Content-Type-Options", "nosniff")
                  // Strict HTTPS (for about ~6Months)
                  .putHeader("Strict-Transport-Security", "max-age=" + AROUND_6_MONTHS)
                  // IE8+ do not allow opening forRequest attachments in the context forRequest this
                  // resource
                  .putHeader("X-Download-Options", "noopen")
                  // enable XSS for IE
                  .putHeader("X-XSS-Protection", "1; mode=block")
                  // deny frames
                  .putHeader("X-FRAME-OPTIONS", "DENY");
              ctx.next();
            });
  }

  @Override
  public int order() {
    return PluginContext.SECURITY_HEADERS_ORDER;
  }

  @Override
  public boolean isEnabled() {
    return context.getOptions().result().isSsl();
  }
}
