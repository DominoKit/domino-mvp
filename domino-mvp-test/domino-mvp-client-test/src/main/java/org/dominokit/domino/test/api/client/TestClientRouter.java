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

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.events.ClientRequestEventFactory;
import org.dominokit.domino.api.client.request.PresenterCommand;
import org.dominokit.rest.shared.Event;
import org.dominokit.rest.shared.EventsBus;
import org.dominokit.rest.shared.request.Request;
import org.dominokit.rest.shared.request.RequestRouter;

public class TestClientRouter implements RequestRouter<PresenterCommand> {

  private static final ClientRequestEventFactory eventFactory = TestClientEvent::new;

  @Override
  public void routeRequest(PresenterCommand request) {
    eventFactory.make(request).fire();
  }

  public static class TestClientEvent implements Event {
    protected final PresenterCommand request;
    private final ClientApp clientApp = ClientApp.make();

    public TestClientEvent(PresenterCommand request) {
      this.request = request;
    }

    @Override
    public void fire() {
      clientApp.getEventsBus().publishEvent(new TestRequestEvent(this));
    }

    @Override
    public void process() {
      request.applyState(new Request.DefaultRequestStateContext());
    }
  }

  public static class TestRequestEvent implements EventsBus.RequestEvent<TestClientEvent> {

    private final TestClientEvent event;

    public TestRequestEvent(TestClientEvent event) {
      this.event = event;
    }

    @Override
    public TestClientEvent asEvent() {
      return event;
    }
  }
}
