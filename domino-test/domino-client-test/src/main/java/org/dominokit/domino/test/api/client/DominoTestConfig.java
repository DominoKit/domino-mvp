package org.dominokit.domino.test.api.client;

import io.vertx.ext.unit.TestContext;
import org.dominokit.domino.api.client.ModuleConfiguration;

import java.util.List;

public interface DominoTestConfig {
    List<ModuleConfiguration> getModules();

    void onBeforeClientStart(DominoTestCase dominoTestCase, ClientContext clientContext, TestContext testContext);

    void onClientStarted(DominoTestCase dominoTestCase, ClientContext clientContext, TestContext testContext);

    void bindSpies(DominoTestCase dominoTestCase);

    void bindFakeViews(DominoTestCase dominoTestCase);
}
