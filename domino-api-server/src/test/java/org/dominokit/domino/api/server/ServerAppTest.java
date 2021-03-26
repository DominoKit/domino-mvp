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
package org.dominokit.domino.api.server;

import static org.junit.Assert.assertTrue;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class ServerAppTest {

  @Rule public RunTestOnContext rule = new RunTestOnContext();

  private ServerApp serverApp;
  private TestRequest request;
  private Vertx vertx;

  @Before
  public void setUp() throws Exception {
    vertx = rule.vertx();
    JsonObject config = new JsonObject();
    config.put("http.port", 0);
    String secretKey = SecretKey.generate();
    RouterConfigurator configurator = new RouterConfigurator(vertx, config, secretKey);
    DominoLauncher.routerHolder.router = configurator.configuredRouter();
    DominoLauncher.configHolder.config = config;
    new DominoLoader(vertx, DominoLauncher.routerHolder.router, DominoLauncher.configHolder.config)
        .start();
    serverApp = ServerApp.make();
    request = new TestRequest();
  }

  @Test
  public void testConfigrationWorks() {
    assertTrue(true);
  }

  @After
  public void tearDown() throws Exception {
    vertx.close();
  }
}
