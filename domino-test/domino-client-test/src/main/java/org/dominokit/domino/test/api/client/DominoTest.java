package org.dominokit.domino.test.api.client;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;

public interface DominoTest {

    DominoTestClient getTestClient();
    JsonObject getAdditionalConfig();
    void onBeforeClientStart(ClientContext clientContext, TestContext testContext);
    void onClientStarted(ClientContext clientContext, TestContext testContext);
    ClientContext getClientContext();
    TestContext getTestContext();

}
