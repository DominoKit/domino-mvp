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

import static java.util.Objects.nonNull;

import com.google.auto.service.AutoService;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import java.nio.file.Paths;
import org.dominokit.domino.api.server.DominoLoaderPlugin;
import org.dominokit.domino.api.server.PluginContext;

@AutoService(DominoLoaderPlugin.class)
public class StaticResourcesConfiguratorPlugin extends BaseDominoLoaderPlugin {

  @Override
  public void applyPlugin(CompleteHandler completeHandler) {
    context.getRouter().route("/").order(Integer.MAX_VALUE - 2).handler(this::serveIndexPage);

    configureNoCacheStaticRoute();
    configureCacheStaticRoute();

    completeHandler.onCompleted();
  }

  private void configureCacheStaticRoute() {
    Route route =
        context
            .getRouter()
            .route("/*")
            .order(Integer.MAX_VALUE)
            .handler(StaticHandler.create().setWebRoot(context.getWebRoot()));
    if (nonNull(System.getProperty("domino.webroot.location"))) {
      StaticHandler webRootStaticHandler = StaticHandler.create();
      webRootStaticHandler.setAllowRootFileSystemAccess(true);
      webRootStaticHandler.setWebRoot(systemWebRoot());
      route.handler(webRootStaticHandler);
    }
    route.handler(this::resourceNotFound).failureHandler(this::resourceNotFound);
  }

  private void configureNoCacheStaticRoute() {
    Route noCacheHandler =
        context
            .getRouter()
            .routeWithRegex("/.*nocache.*")
            .order(Integer.MAX_VALUE - 1)
            .handler(
                StaticHandler.create().setCachingEnabled(false).setWebRoot(context.getWebRoot()));
    if (nonNull(System.getProperty("domino.webroot.location"))) {
      StaticHandler webRootStaticHandler = StaticHandler.create();
      webRootStaticHandler.setAllowRootFileSystemAccess(true);
      webRootStaticHandler.setWebRoot(systemWebRoot());
      webRootStaticHandler.setCachingEnabled(false);
      noCacheHandler.handler(webRootStaticHandler);
    }

    noCacheHandler.handler(this::resourceNotFound).failureHandler(this::resourceNotFound);
  }

  private HttpServerResponse resourceNotFound(RoutingContext routingContext) {
    return serveIndexPage(routingContext, 404);
  }

  private String systemWebRoot() {
    return Paths.get(System.getProperty("domino.webroot.location")).toAbsolutePath().toString();
  }

  private HttpServerResponse serveIndexPage(RoutingContext routingContext) {
    return serveIndexPage(routingContext, 200);
  }

  private HttpServerResponse serveIndexPage(RoutingContext routingContext, int statusCode) {
    if (context.getConfig().getBoolean("serve.index.page", true)) {
      return IndexPageContext.INSTANCE
          .getIndexPageProvider()
          .serveIndexPage(context, routingContext, statusCode);
    } else {
      HttpServerResponse httpServerResponse = routingContext.response().setStatusCode(statusCode);
      httpServerResponse.end();
      return httpServerResponse;
    }
  }

  @Override
  public int order() {
    return PluginContext.STATIC_RESOURCES_ORDER;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
