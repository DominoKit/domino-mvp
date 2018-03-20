package org.dominokit.domino.api.server;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public interface CSRFWhiteListHandler {

    boolean whiteList(RoutingContext context, JsonObject config);
    boolean match(RoutingContext context, JsonObject config);
}
