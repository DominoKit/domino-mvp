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
package org.dominokit.domino.api.client.events;

import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.dominokit.domino.api.shared.extension.ActivationEvent;
import org.dominokit.domino.api.shared.extension.ContextAggregator;
import org.dominokit.domino.history.DominoHistory;

public class BaseRoutingAggregator {

  private static final Logger LOGGER = Logger.getLogger(BaseRoutingAggregator.class.getName());

  private final List<Class<? extends ActivationEvent>> events;
  private ContextAggregator.ContextWait<DominoHistory.State> routingEvent =
      ContextAggregator.ContextWait.create();

  private ContextAggregator contextAggregator;
  private boolean autoResetRoutingEvent = true;

  protected BaseRoutingAggregator(List<Class<? extends ActivationEvent>> events) {
    this.events = events;
  }

  public void init(Consumer<DominoHistory.State> stateConsumer, boolean autoResetRoutingEvent) {
    this.autoResetRoutingEvent = autoResetRoutingEvent;
    init(stateConsumer);
  }

  public void init(Consumer<DominoHistory.State> stateConsumer) {
    contextAggregator =
        ContextAggregator.waitFor(routingEvent)
            .onReady(
                () -> {
                  stateConsumer.accept(routingEvent.get());
                  if (autoResetRoutingEvent) {
                    contextAggregator.resetContext(routingEvent);
                  }
                });
    events.stream().map(ActivationEventWait::new).forEach(w -> w.setAggregator(contextAggregator));
  }

  public void completeRoutingState(DominoHistory.State state) {
    routingEvent.complete(state);
  }

  public void resetRoutingState() {
    contextAggregator.resetContext(routingEvent);
  }
}
