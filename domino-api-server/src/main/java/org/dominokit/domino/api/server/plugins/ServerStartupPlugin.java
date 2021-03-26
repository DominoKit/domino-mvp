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
import io.vertx.core.AsyncResult;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.reactivex.core.http.HttpServer;
import java.util.function.Consumer;
import org.dominokit.domino.api.server.DominoLoaderPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(DominoLoaderPlugin.class)
public class ServerStartupPlugin extends BaseDominoLoaderPlugin {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServerStartupPlugin.class);

  @Override
  protected void applyPlugin(CompleteHandler completeHandler) {
    startHttpServer(context.getOptions(), context.getHttpServerConsumer());
  }

  private void startHttpServer(
      AsyncResult<HttpServerOptions> options, Consumer<HttpServer> httpServerConsumer) {

    LOGGER.info("HTTP Server options  : " + options.result().getPort());
    context
        .getVertx()
        .createHttpServer(options.result())
        .requestHandler(context.getRouter())
        .listen(
            options.result().getPort(),
            event -> {
              if (event.succeeded()) {
                LOGGER.info("Server started on port : " + event.result().actualPort());
                httpServerConsumer.accept(HttpServer.newInstance(event.result()));
              } else {
                LOGGER.error("Failed to start server", event.cause());
              }
            });
  }

  @Override
  public int order() {
    return Integer.MAX_VALUE;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
