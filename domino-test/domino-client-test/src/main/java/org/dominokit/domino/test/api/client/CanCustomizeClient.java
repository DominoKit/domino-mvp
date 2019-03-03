package org.dominokit.domino.test.api.client;

import io.vertx.ext.unit.TestContext;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.test.api.DominoTestServer;

public interface CanCustomizeClient extends CanStartClient {
    CanCustomizeClient overrideConfig(DominoTestClient.ConfigOverrideHandler handler);

    <L extends DominoEventListener> CanCustomizeClient listenerOf(Class<L> listenerType,
                                                                  ListenerHandler<L> handler);

    CanCustomizeClient onBeforeClientStart(BeforeClientStart beforeClientStart);

    CanCustomizeClient onClientStarted(StartCompleted startCompleted);

    CanCustomizeClient withServer(TestContext testContext, DominoTestServer.AfterLoadHandler afterLoadHandler);

    CanCustomizeClient withServer(TestContext testContext);
}
