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
    public HttpServerResponse serveIndexPage(PluginContext context, RoutingContext routingContext, int statusCode) {
        LOGGER.info("Loading index page using Default provider...");
        return routingContext
                .response()
                .setStatusCode(statusCode)
                .putHeader("Content-type", "text/html")
                .sendFile(context.getWebRoot() + "/index.html");
    }
}
