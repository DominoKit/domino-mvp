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
package org.dominokit.domino.test.api;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestConfigReader {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestConfigReader.class);
  private JsonObject testConfig;

  public TestConfigReader(Vertx vertx, String fileName) {
    try {
      testConfig = vertx.fileSystem().readFileBlocking(fileName).toJsonObject();
    } catch (Exception e) {
      LOGGER.warn("no test config.json file provided", e.getLocalizedMessage());
      testConfig = new JsonObject();
    }
  }

  public JsonObject getTestConfig() {
    return testConfig;
  }

  public String getConfigValue(String configKey) {
    return testConfig.getString(configKey);
  }
}
