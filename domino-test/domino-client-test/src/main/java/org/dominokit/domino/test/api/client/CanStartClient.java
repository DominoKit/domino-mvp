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

public interface CanStartClient {
  void start();

  void start(DominoTestClient.StartCompletedHandler onCompletedHandler);

  void start(String configFileName);

  void start(String configFileName, DominoTestClient.StartCompletedHandler onCompletedHandler);

  void start(JsonObject additionalConfig);

  void start(
      JsonObject additionalConfig, DominoTestClient.StartCompletedHandler onCompletedHandler);

  void start(String configFileName, JsonObject additionalConfig);

  void start(
      String configFileName,
      JsonObject additionalConfig,
      DominoTestClient.StartCompletedHandler onCompletedHandler);
}
