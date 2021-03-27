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
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.test.history.TestDominoHistory;
import org.dominokit.rest.DominoRestConfig;
import org.dominokit.rest.shared.EventProcessor;

public class TestClientAppFactory {

  protected static TestServerRouter serverRouter;
  protected static TestInMemoryEventsListenersRepository listenersRepository;
  protected static TestDominoHistory history;
  protected static TestClientRouter clientRouter;
  protected static EventProcessor requestEventProcessor;
  protected static TestEventBus eventBus;
  protected static FakeDominoOptions dominoOptions;

  private TestClientAppFactory() {}

  public static ClientApp make(Vertx vertx) {

    clientRouter = new TestClientRouter();
    serverRouter = new TestServerRouter(vertx);
    requestEventProcessor = new EventProcessor();
    eventBus = new TestEventBus(requestEventProcessor);

    listenersRepository = new TestInMemoryEventsListenersRepository();
    history = new TestDominoHistory();
    dominoOptions = new FakeDominoOptions();
    DominoRestConfig.initDefaults();

    ClientApp clientApp =
        ClientApp.ClientAppBuilder.clientRouter(clientRouter)
            .eventsBus(eventBus)
            .eventsListenersRepository(listenersRepository)
            .history(history)
            .asyncRunner(new TestAsyncRunner())
            .dominoOptions(dominoOptions)
            .slotsManager(new TestSlotsManager())
            .build();

    return clientApp;
  }
}
