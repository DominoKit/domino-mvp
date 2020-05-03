package org.dominokit.domino.api.server.plugins;

import io.vertx.core.http.HttpServerResponse;
import org.dominokit.domino.api.server.PluginContext;
import io.vertx.ext.web.RoutingContext;

public interface IndexPageProvider {
    HttpServerResponse serveIndexPage(PluginContext context, RoutingContext routingContext, int statusCode);
}