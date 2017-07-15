package com.progressoft.brix.domino.test.api.client;

import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.server.entrypoint.VertxEntryPointContext;
import io.vertx.core.Vertx;

public interface ClientContext {
    TestDominoHistory history();
    void setRoutingListener(TestServerRouter.RoutingListener routingListener);
    void removeRoutingListener();
    DominoTestClient.TestResponse forRequest(Class<? extends ClientServerRequest> request);
    Vertx vertx();
    VertxEntryPointContext vertxEntryPointContext();
}
