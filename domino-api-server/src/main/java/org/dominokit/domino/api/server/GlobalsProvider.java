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
package org.dominokit.domino.api.server;

import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;

public class GlobalsProvider {

  public static final GlobalsProvider INSTANCE = new GlobalsProvider();
  private JsonObject config;
  private Vertx vertx;
  private Router router;

  private GlobalsProvider() {}

  void setConfig(JsonObject config) {
    this.config = config;
  }

  void setVertx(Vertx vertx) {
    this.vertx = vertx;
  }

  void setRouter(Router router) {
    this.router = router;
  }

  public JsonObject getConfig() {
    return config;
  }

  public Vertx getVertx() {
    return vertx;
  }

  public Router getRouter() {
    return router;
  }
}
