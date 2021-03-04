package org.dominokit.domino.api.server;

import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;

public class GlobalsProvider {

    public static final GlobalsProvider INSTANCE = new GlobalsProvider();
    private JsonObject config;
    private Vertx vertx;
    private Router router;

    private GlobalsProvider() {
    }

    void setConfig(JsonObject config) {
        this.config = config;
    }

    void setVertx(Vertx vertx) {
        this.vertx = vertx;
    }

    void setRouter(Router router) {
        this.router = router;
    }

    public JsonObject getConfig() {
        return config;
    }

    public Vertx getVertx() {
        return vertx;
    }

    public Router getRouter() {
        return router;
    }
}
