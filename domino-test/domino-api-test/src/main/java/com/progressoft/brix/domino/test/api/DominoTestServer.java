package com.progressoft.brix.domino.test.api;

import com.progressoft.brix.domino.api.server.DominoLoader;
import com.progressoft.brix.domino.api.server.RouterConfigurator;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.web.Router;

public class DominoTestServer {

    private int actualPort;
    private Router router;
    private JsonObject config;
    private Vertx vertx;

    public DominoTestServer(Vertx vertx) {
        this.vertx = vertx;
    }

    public void start(TestContext context) {
        RouterConfigurator routerConfigurator = new RouterConfigurator(vertx);
        router = routerConfigurator.configuredRouter();
        config = new JsonObject().put("http.port", 0);
        onBeforeDominoLoad(router, config);
        new DominoLoader(vertx, router, config).start(context.asyncAssertSuccess(server -> actualPort = server.actualPort()));
        onAfterDominoLoad(router, config);
    }

    public void onBeforeDominoLoad(Router router, JsonObject config) {
        //can be implemented by the test case if needed
    }

    public void onAfterDominoLoad(Router router, JsonObject config) {
        //can be implemented by the test case if needed
    }

    public Vertx getVertx() {
        return vertx;
    }

    public Router getRouter() {
        return router;
    }

    public JsonObject getConfig() {
        return config;
    }

    public int getActualPort() {
        return actualPort;
    }

}
