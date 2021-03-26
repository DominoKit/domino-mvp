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
package org.dominokit.domino.client.commons.request;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.async.AsyncRunner;
import org.dominokit.domino.api.client.events.ClientRequestEventFactory;
import org.dominokit.domino.api.client.request.PresenterCommand;
import org.dominokit.domino.rest.shared.request.RequestRouter;

public class ClientRouter implements RequestRouter<PresenterCommand> {

  private static final Logger LOGGER = Logger.getLogger(ClientRouter.class.getName());

  private final ClientRequestEventFactory requestEventFactory;

  public ClientRouter(ClientRequestEventFactory requestEventFactory) {
    this.requestEventFactory = requestEventFactory;
  }

  @Override
  public void routeRequest(final PresenterCommand presenterCommand) {

    ClientApp.make()
        .getAsyncRunner()
        .runAsync(
            new AsyncRunner.AsyncTask() {
              @Override
              public void onSuccess() {
                requestEventFactory.make(presenterCommand).fire();
              }

              @Override
              public void onFailed(Throwable error) {
                LOGGER.log(
                    Level.SEVERE, "Could not RunAsync request [" + presenterCommand + "]", error);
              }
            });
  }
}
