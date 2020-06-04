package org.dominokit.domino.api.server;

import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

@FunctionalInterface
public interface VertxOptionsHandler {
    void onBeforeVertxStart(VertxOptions options, JsonObject config);
}
