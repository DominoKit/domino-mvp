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

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.dominokit.domino.api.server.PluginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultIndexPageProvider implements IndexPageProvider {

  public static final Logger LOGGER = LoggerFactory.getLogger(DefaultIndexPageProvider.class);

  public static final IndexPageProvider INSTANCE = new DefaultIndexPageProvider();

  @Override
  public HttpServerResponse serveIndexPage(
      PluginContext context, RoutingContext routingContext, int statusCode) {
    LOGGER.info("Loading index page using Default provider...");
    return routingContext
        .response()
        .setStatusCode(statusCode)
        .putHeader("Content-type", "text/html")
        .sendFile(context.getWebRoot() + "/index.html");
  }
}
