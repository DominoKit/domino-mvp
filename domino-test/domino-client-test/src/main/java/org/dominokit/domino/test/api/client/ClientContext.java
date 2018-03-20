package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.server.entrypoint.VertxEntryPointContext;
import io.vertx.core.Vertx;

public interface ClientContext {
    TestDominoHistory history();

    void setRoutingListener(TestServerRouter.RoutingListener routingListener);

    TestRoutingListener getDefaultRoutingListener();

    void removeRoutingListener();

    DominoTestClient.TestResponse forRequest(String requestKey);

    DominoTestClient.TestResponse forRequest(Class<? extends ServerRequest> request);

    Vertx vertx();

    VertxEntryPointContext vertxEntryPointContext();
}
