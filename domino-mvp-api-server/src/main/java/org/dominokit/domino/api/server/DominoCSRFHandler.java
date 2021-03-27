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
package org.dominokit.domino.api.server;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.CSRFHandlerImpl;
import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

public class DominoCSRFHandler extends CSRFHandlerImpl {
  private final JsonObject config;
  private Set<CSRFWhiteListHandler> whiteListHandlers;

  DominoCSRFHandler(String secret, JsonObject config) {
    super(secret);
    this.config = config;
    this.whiteListHandlers = new LinkedHashSet<>();
    ServiceLoader.load(CSRFWhiteListHandler.class).forEach(h -> whiteListHandlers.add(h));
  }

  @Override
  public void handle(RoutingContext ctx) {

    HttpMethod method = ctx.request().method();

    switch (method) {
      case POST:
      case PUT:
      case DELETE:
      case PATCH:
        final Set<CSRFWhiteListHandler> blackList =
            whiteListHandlers.stream()
                .filter(w -> w.match(ctx, config) && !w.whiteList(ctx, config))
                .collect(Collectors.toSet());
        if (blackList.isEmpty()) ctx.next();
        else super.handle(ctx);
        break;
      default:
        super.handle(ctx);
        break;
    }
  }
}
