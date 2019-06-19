package org.dominokit.domino.test.api.client;

import io.vertx.core.Vertx;
import org.dominokit.domino.api.server.entrypoint.VertxEntryPointContext;
import org.dominokit.domino.rest.shared.request.ServerRequest;
import org.dominokit.domino.test.history.TestDominoHistory;

public interface ClientContext {
    TestDominoHistory history();

    void setRoutingListener(TestServerRouter.RoutingListener routingListener);

    TestRoutingListener getDefaultRoutingListener();

    void removeRoutingListener();

    DominoTestClient.TestResponse forRequest(String requestKey);

    DominoTestClient.TestResponse forRequest(Class<? extends ServerRequest> request);

    Vertx vertx();

    VertxEntryPointContext vertxEntryPointContext();

    FakeDominoOptions getDominoOptions();

    void registerStore(String key, Object data);
}
