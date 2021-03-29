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

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.reactivex.core.http.HttpServer;
import java.util.function.Consumer;
import org.dominokit.domino.api.server.entrypoint.VertxContext;

public class PluginContext {

  public static final int MODULES_LOADER_ORDER = 0;
  public static final int LOG_REQUESTS_ORDER = 10;
  public static final int SECURITY_HEADERS_ORDER = 20;
  public static final int AUTH_ORDER = 30;
  public static final int RESTEASY_ORDER = 40;
  public static final int WEBJARS_RESOURCES_HOLDER = 90;
  public static final int STATIC_RESOURCES_ORDER = 100;

  private final int defaultPort;
  private final String httpPortKey;
  private final String webRoot;
  private final Vertx vertx;
  private final Router router;
  private final io.vertx.reactivex.core.Vertx rxVertx;
  private final io.vertx.reactivex.ext.web.Router rxRouter;
  private final JsonObject config;
  private final VertxContext vertxContext;
  private final AsyncResult<HttpServerOptions> options;
  private final Consumer<HttpServer> httpServerConsumer;

  public PluginContext(
      IsDominoLoader dominoLoader,
      VertxContext vertxContext,
      AsyncResult<HttpServerOptions> options,
      Consumer<HttpServer> httpServerConsumer) {

    this.defaultPort = dominoLoader.getDefaultPort();
    this.httpPortKey = dominoLoader.getHttpPortKey();
    this.webRoot = dominoLoader.getWebroot();
    this.vertx = dominoLoader.getVertx();
    this.router = dominoLoader.getRouter();
    this.rxVertx = dominoLoader.getRxVertx();
    this.rxRouter = dominoLoader.getRxRouter();
    this.config = dominoLoader.getConfig();
    this.vertxContext = vertxContext;
    this.options = options;
    this.httpServerConsumer = httpServerConsumer;
  }

  public int getDefaultPort() {
    return defaultPort;
  }

  public String getHttpPortKey() {
    return httpPortKey;
  }

  public String getWebRoot() {
    return webRoot;
  }

  public Vertx getVertx() {
    return vertx;
  }

  public Router getRouter() {
    return router;
  }

  public io.vertx.reactivex.core.Vertx getRxVertx() {
    return rxVertx;
  }

  public io.vertx.reactivex.ext.web.Router getRxRouter() {
    return rxRouter;
  }

  public JsonObject getConfig() {
    return config;
  }

  public VertxContext getVertxContext() {
    return vertxContext;
  }

  public AsyncResult<HttpServerOptions> getOptions() {
    return options;
  }

  public Consumer<HttpServer> getHttpServerConsumer() {
    return httpServerConsumer;
  }
}
