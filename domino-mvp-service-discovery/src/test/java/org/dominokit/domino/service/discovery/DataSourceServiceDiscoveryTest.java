/*
 * Copyright © 2019 Dominokit
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

import io.vertx.core.json.JsonObject;
import org.dominokit.domino.service.discovery.configuration.DataSourceServiceConfiguration;

public abstract class DataSourceServiceDiscoveryTest extends BaseVertxServiceDiscoveryTest {

  private static final String SERVICE_URL = "service url";

  protected DataSourceServiceConfiguration dataSourceConfiguration;
  protected JsonObject dataSourceLocation;

  @Override
  protected void onSetup() {
    dataSourceLocation = new JsonObject().put("url", SERVICE_URL);
    dataSourceConfiguration = new DataSourceServiceConfiguration(SERVICE_NAME, dataSourceLocation);
  }
}
