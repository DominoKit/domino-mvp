package com.progressoft.brix.domino.test.api.client;

import com.progressoft.brix.domino.api.server.entrypoint.VertxEntryPointContext;
import io.vertx.core.Vertx;

public interface ClientContext {
    TestDominoHistory history();
    void setRoutingListener(TestServerRouter.RoutingListener routingListener);
    TestRoutingListener getDefaultRoutingListener();
    void removeRoutingListener();
    DominoTestClient.TestResponse forRequest(String requestKey);
    Vertx vertx();
    VertxEntryPointContext vertxEntryPointContext();
}
