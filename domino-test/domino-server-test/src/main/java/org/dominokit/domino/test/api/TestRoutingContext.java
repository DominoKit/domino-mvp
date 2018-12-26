package org.dominokit.domino.test.api;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public interface TestRoutingContext {
    Router getRouter();

    JsonObject getConfig();
}
