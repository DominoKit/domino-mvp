package org.dominokit.domino.api.server;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.dominokit.domino.api.server.entrypoint.VertxContext;

public interface IsDominoLoader {

    int getDefaultPort();

    String getHttpPortKey();

    String getWebroot();

    Vertx getVertx();

    Router getRouter();

    JsonObject getConfig();

    io.vertx.reactivex.core.Vertx getRxVertx();

    io.vertx.reactivex.ext.web.Router getRxRouter();
}
