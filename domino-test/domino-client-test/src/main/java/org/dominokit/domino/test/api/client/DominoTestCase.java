package org.dominokit.domino.test.api.client;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.Before;

public abstract class DominoTestCase implements DominoTest {

    protected DominoTestClient testClient;
    private DominoTestConfig testConfig;
    private TestContext testContext;
    protected ClientContext clientContext;

    public DominoTestCase(DominoTestConfig testConfig) {
        this.testConfig = testConfig;
    }

    @Before
    public void prepareTestClient(TestContext testContext) {
        this.testContext = testContext;
        testClient = (DominoTestClient) DominoTestClient.useModules(testConfig.getModules());
        testClient.setTestContext(testContext);
        this.clientContext = testClient;
        testClient
                .onBeforeClientStart(clientContext -> {
                    testConfig.bindSpies(DominoTestCase.this);
                    testConfig.bindFakeViews(DominoTestCase.this);
                    onBeforeClientStart(clientContext, testContext);
                })
                .onClientStarted(context -> {
                    onClientStarted(clientContext, testContext);
                });
    }

    @Override
    public DominoTestClient getTestClient() {
        return testClient;
    }

    @Override
    public JsonObject getAdditionalConfig() {
        return new JsonObject();
    }

    @Override
    public void onBeforeClientStart(ClientContext clientContext, TestContext testContext) {
        testConfig.onBeforeClientStart(this, clientContext, testContext);
    }

    @Override
    public void onClientStarted(ClientContext clientContext, TestContext testContext) {
        testConfig.onClientStarted(this, clientContext, testContext);
    }

    @Override
    public ClientContext getClientContext() {
        return this.clientContext;
    }

    @Override
    public TestContext getTestContext() {
        return testContext;
    }
}
