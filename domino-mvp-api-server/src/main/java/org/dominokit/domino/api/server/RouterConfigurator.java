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

import static java.util.Objects.nonNull;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.dominokit.domino.api.server.logging.DefaultRemoteLogger;
import org.dominokit.domino.api.server.logging.RemoteLogger;
import org.dominokit.domino.api.server.logging.RemoteLoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RouterConfigurator {

  private static final Logger LOGGER = LoggerFactory.getLogger(RouterConfigurator.class);

  private static final int DEFAULT_BODY_LIMIT = 50;
  private static final long MB = 1048576L;
  private static final String REMOTE_LOGGER = "remote.logger";

  private final Vertx vertx;
  private final JsonObject config;
  private final String secret;
  private boolean clustered;

  public RouterConfigurator(Vertx vertx, JsonObject config, String secret) {
    this.vertx = vertx;
    this.config = config;
    this.secret = secret;
  }

  public Router configuredRouter() {
    return makeRouterWithPredefinedHandlers(vertx);
  }

  public Router configuredClusteredRouter() {
    this.clustered = true;
    return makeRouterWithPredefinedHandlers(vertx);
  }

  private Router makeRouterWithPredefinedHandlers(Vertx vertx) {
    Router router = Router.router(vertx);
    addPredefinedHandlers(vertx, router);
    return router;
  }

  private void addPredefinedHandlers(Vertx vertx, Router router) {
    addCorsHandler(router);
    addSessionHandler(vertx, router);
    addCSRFHandler(router);
    //        addRemoteExceptionHandler(router);
  }

  private void addCorsHandler(Router router) {

    boolean enableCors =
        (nonNull(System.getenv("cors.enabled"))
                && Boolean.parseBoolean(System.getenv("cors.enabled")))
            || (nonNull(System.getProperty("cors.enabled"))
                && Boolean.parseBoolean(System.getProperty("cors.enabled")))
            || config.getBoolean("cors.enabled", false);
    if (enableCors) {
      LOGGER.info("CORS is enabled > allowed origins:*, Allowed headers:*, Allowed methods: ALL");
      router
          .route()
          .handler(
              CorsHandler.create("*")
                  .allowedHeaders(
                      new HashSet<>(
                          Arrays.asList(
                              "Content-Type",
                              "X-HTTP-Method-Override",
                              "X-XSRF-TOKEN",
                              "Accept",
                              "cache-control")))
                  .allowedMethods(Stream.of(HttpMethod.values()).collect(Collectors.toSet())));
    }
  }

  private void addRemoteExceptionHandler(Router router) {
    if (config.getBoolean("remove.exception.handler.enabled", true)) {
      RemoteLogger remoteLogger =
          StreamSupport.stream(ServiceLoader.load(RemoteLogger.class).spliterator(), false)
              .filter(logger -> logger.getClass().getName().equals(config.getString(REMOTE_LOGGER)))
              .findFirst()
              .orElseGet(DefaultRemoteLogger::new);
      router.route("/logging/remoteLogging").handler(new RemoteLoggingHandler(remoteLogger));
    }
  }

  private void addCSRFHandler(Router router) {
    if (config.getBoolean("csrf.enabled", true)) {
      router.route().handler(new DominoCSRFHandler(secret, config));
    }
  }

  private void addSessionHandler(Vertx vertx, Router router) {
    if (config.getBoolean("session.enabled", true)) {
      SessionStore sessionStore =
          clustered ? ClusteredSessionStore.create(vertx) : LocalSessionStore.create(vertx);
      if (config.getBoolean("ssl.enabled", false)) {
        router
            .route()
            .handler(
                SessionHandler.create(sessionStore)
                    .setCookieHttpOnlyFlag(true)
                    .setCookieSecureFlag(true));
      } else {
        router.route().handler(SessionHandler.create(sessionStore));
      }
    }
  }
}
