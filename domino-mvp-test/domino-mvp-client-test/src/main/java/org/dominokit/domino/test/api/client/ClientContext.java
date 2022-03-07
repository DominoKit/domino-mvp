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
package org.dominokit.domino.test.api.client;

import io.vertx.core.Vertx;
import org.dominokit.domino.api.server.entrypoint.VertxEntryPointContext;
import org.dominokit.domino.test.history.TestDominoHistory;
import org.dominokit.rest.shared.request.ServerRequest;

public interface ClientContext {
  TestDominoHistory history();

  void setRoutingListener(TestServerRouter.RoutingListener routingListener);

  TestRoutingListener getDefaultRoutingListener();

  void removeRoutingListener();

  DominoTestClient.TestResponse forRequest(String requestKey);

  DominoTestClient.TestResponse forRequest(Class<? extends ServerRequest> request);

  Vertx vertx();

  VertxEntryPointContext vertxEntryPointContext();

  FakeDominoOptions getDominoOptions();
}
