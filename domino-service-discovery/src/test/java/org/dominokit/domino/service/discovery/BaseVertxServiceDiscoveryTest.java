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
package org.dominokit.domino.service.discovery;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import java.util.function.Function;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public abstract class BaseVertxServiceDiscoveryTest {

  protected static final String SERVICE_NAME = "service name";
  protected static final String SERVICE_HOST = "service host";
  protected static final String ONLY_SPACES = "    ";
  protected static final String EMPTY_STRING = "";
  protected static final String SERVICE_ADDRESS = "service address";
  protected static final String VALUE = "value";
  protected static final String KEY = "key";

  @Rule public RunTestOnContext rule = new RunTestOnContext();

  protected ServiceDiscovery serviceDiscovery;
  protected VertxServiceDiscovery vertxServiceDiscovery;
  protected Vertx vertx;
  protected JsonObject metadata;
  protected JsonObject jsonFilter;

  @Before
  public void setUp() throws Exception {
    vertx = rule.vertx();
    serviceDiscovery = ServiceDiscovery.create(vertx);
    metadata = new JsonObject().put(KEY, VALUE);
    jsonFilter = new JsonObject().put("name", SERVICE_NAME);
    vertxServiceDiscovery = new VertxServiceDiscovery(vertx);
    onSetup();
  }

  @After
  public void tearDown() throws Exception {
    vertx.close();
  }

  protected Function<Record, Boolean> filter() {
    return record -> record.getName().equals(SERVICE_NAME);
  }

  protected abstract void onSetup();
}
