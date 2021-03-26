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
package org.dominokit.domino.api.server.entrypoint;

import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import org.dominokit.domino.api.server.config.ServerConfiguration;

public class VertxEntryPointContext implements ServerEntryPointContext {

  private final RoutingContext routingContext;
  private final ServerConfiguration serverConfiguration;
  private final Vertx vertx;

  public VertxEntryPointContext(
      RoutingContext routingContext, ServerConfiguration serverConfiguration, Vertx vertx) {
    this.routingContext = routingContext;
    this.serverConfiguration = serverConfiguration;
    this.vertx = vertx;
  }

  public RoutingContext getRoutingContext() {
    return routingContext;
  }

  public ServerConfiguration config() {
    return serverConfiguration;
  }

  public Vertx vertx() {
    return vertx;
  }

  @Override
  public String getName() {
    return VertxEntryPointContext.class.getCanonicalName();
  }
}
