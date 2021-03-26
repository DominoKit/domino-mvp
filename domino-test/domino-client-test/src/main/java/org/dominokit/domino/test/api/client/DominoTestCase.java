/*
 * Copyright © ${year} Dominokit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dominokit.domino.test.api.client;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import org.junit.Before;

public abstract class DominoTestCase implements DominoTest {

  protected DominoTestClient testClient;
  private DominoTestConfig testConfig;
  private TestContext testContext;
  protected ClientContext clientContext;
  protected TestRoutingListener requests;

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
        .onBeforeClientStart(
            clientContext -> {
              testConfig.bindSpies(DominoTestCase.this);
              testConfig.bindFakeViews(DominoTestCase.this);
              this.requests = clientContext.getDefaultRoutingListener();
              onBeforeClientStart(clientContext, testContext);
            })
        .onClientStarted(
            context -> {
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
