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
package org.dominokit.domino.api.client.startup;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.events.BaseRoutingAggregator;
import org.dominokit.domino.api.client.mvp.presenter.BaseClientPresenter;
import org.dominokit.domino.history.DominoHistory;
import org.dominokit.domino.history.TokenFilter;

public abstract class BaseRoutingStartupTask implements ClientStartupTask, PresenterRoutingTask {

  private static final Logger LOGGER = Logger.getLogger(BaseRoutingStartupTask.class.getName());

  protected List<BaseRoutingAggregator> aggregators = new ArrayList<>();
  protected boolean enabled = true;
  protected BaseClientPresenter presenter;

  public BaseRoutingStartupTask(List<? extends BaseRoutingAggregator> aggregators) {
    this.aggregators.addAll(aggregators);
    aggregators.forEach(
        aggregator ->
            aggregator.init(
                state -> {
                  onStateReady(state);
                  resetRouting();
                }));
  }

  private void resetRouting() {
    aggregators.forEach(BaseRoutingAggregator::resetRoutingState);
  }

  protected void bindPresenter(BaseClientPresenter presenter) {
    presenter.setRoutingTask(this);
    this.presenter = presenter;
  }

  @Override
  public void execute() {
    ClientApp.make()
        .getHistory()
        .listen(
            getTokenFilter(),
            state -> {
              if (isNull(presenter) || !presenter.isActivated()) {
                doRoutingIfEnabled(state);
              } else {
                if (isReRouteActivated()) {
                  doRoutingIfEnabled(state);
                }
              }
            },
            isRoutingOnce())
        .onDirectUrl(getStartupTokenFilter());
  }

  protected void doRoutingIfEnabled(DominoHistory.State state) {
    if (enabled) {
      aggregators.forEach(aggregator -> aggregator.completeRoutingState(state));
    } else {
      if (nonNull(presenter)) {
        presenter.onSkippedRouting();
      }
    }
  }

  protected abstract void onStateReady(DominoHistory.State state);

  protected abstract TokenFilter getTokenFilter();

  protected TokenFilter getStartupTokenFilter() {
    return getTokenFilter();
  }

  protected boolean isRoutingOnce() {
    return false;
  }

  @Override
  public void disable() {
    this.enabled = false;
  }

  @Override
  public void enable() {
    this.enabled = true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  protected boolean isReRouteActivated() {
    return false;
  }
}
