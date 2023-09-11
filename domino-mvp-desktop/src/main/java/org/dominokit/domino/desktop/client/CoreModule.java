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
package org.dominokit.domino.desktop.client;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.async.AsyncRunner;
import org.dominokit.domino.api.client.extension.InMemoryDominoEventsListenerRepository;
import org.dominokit.domino.client.commons.request.ClientRouter;
import org.dominokit.domino.desktop.client.events.DesktopClientEventFactory;
import org.dominokit.domino.desktop.client.events.DesktopEventBus;

public class CoreModule {

  private CoreModule() {}

  public static void init() {
    ClientRouter clientRouter = new ClientRouter(new DesktopClientEventFactory());
    ClientApp.ClientAppBuilder.clientRouter(clientRouter)
        .eventsBus(new DesktopEventBus())
        .eventsListenersRepository(new InMemoryDominoEventsListenerRepository())
        .history(new DesktopStateHistory())
        .asyncRunner(AsyncRunner.AsyncTask::onSuccess)
        .dominoOptions(new DesktopDominoOptions())
        .slotsManager(new DesktopSlotsManager())
        .presentersNamesRegistry(new DesktopNamedPresenters())
        .build();
  }
}
