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
package org.dominokit.domino.plugins.resteasy;

import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.dominokit.domino.api.server.entrypoint.VertxContext;

public class ResourceContext {

  private final Vertx rxVertx;
  private final Router rxRouter;
  private final VertxContext vertxContext;
  private final RoutingContext routingContext;

  ResourceContext(
      Vertx rxVertx, Router rxRouter, VertxContext vertxContext, RoutingContext routingContext) {
    this.rxVertx = rxVertx;
    this.rxRouter = rxRouter;
    this.vertxContext = vertxContext;
    this.routingContext = routingContext;
  }

  public Vertx getRxVertx() {
    return rxVertx;
  }

  public Router getRxRouter() {
    return rxRouter;
  }

  public VertxContext getVertxContext() {
    return vertxContext;
  }

  public RoutingContext getRoutingContext() {
    return routingContext;
  }
}
