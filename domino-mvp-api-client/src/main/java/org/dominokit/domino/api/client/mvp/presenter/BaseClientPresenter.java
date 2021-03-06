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
package org.dominokit.domino.api.client.mvp.presenter;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.async.AsyncRunner;
import org.dominokit.domino.api.client.extension.DominoEvents;
import org.dominokit.domino.api.client.mvp.IsStore;
import org.dominokit.domino.api.client.mvp.RegistrationHandler;
import org.dominokit.domino.api.client.mvp.StoreRegistry;
import org.dominokit.domino.api.client.startup.PresenterRoutingTask;
import org.dominokit.domino.api.shared.extension.DominoEvent;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.extension.GlobalDominoEventListener;
import org.dominokit.domino.history.AppHistory;
import org.dominokit.domino.history.DominoHistory;
import org.dominokit.domino.history.TokenParameter;

public abstract class BaseClientPresenter extends ClientPresenter implements Presentable {

  private static final Logger LOGGER = Logger.getLogger(BaseClientPresenter.class.getName());
  private PresenterState state;
  protected boolean activated;
  private PresenterRoutingTask routingTask;

  private final PresenterState initialized =
      () ->
          LOGGER.info(
              "Presenter "
                  + BaseClientPresenter.this.getClass()
                  + " Have already been initialized.");

  private final PresenterState uninitialized = this::initialize;

  private Map<Class<? extends DominoEvent>, DominoEventListener> listeners;
  private Map<Class<? extends DominoEvent>, GlobalDominoEventListener> globalListeners;
  private final List<RegistrationHandler> storeRegisterations = new ArrayList<>();

  protected void initialize() {
    postConstruct();
    this.listeners = getListeners();
    this.globalListeners = getGlobalListeners();
    registerListeners();
    registerGlobalListeners();
    state = initialized;
    if (isAutoActivate()) {
      activate();
    }
  }

  protected void postConstruct() {}

  protected void activate() {
    activated = true;
    fireStateEvent(true);
    onActivated();
  }

  protected void fireStateEvent(boolean state) {
    fireActivationEvent(state);
  }

  protected void fireActivationEvent(boolean state) {}

  private void registerListeners() {
    listeners.forEach((key, value) -> ClientApp.make().registerEventListener(key, value));
  }

  private void registerGlobalListeners() {
    globalListeners.forEach(
        (key, value) -> ClientApp.make().registerGlobalEventListener(key, value));
  }

  private void removeListeners() {
    listeners.forEach((key, value) -> ClientApp.make().removeEventListener(key, value));
  }

  private void removeGlobalListeners() {
    globalListeners.forEach((key, value) -> ClientApp.make().removeGlobalEventListener(key, value));
  }

  @Override
  public Presentable init() {
    this.state = uninitialized;
    prepare();
    return this;
  }

  @Override
  public ClientPresenter prepare() {
    state.process();
    return this;
  }

  protected final void deActivate() {
    removeListeners();
    removeGlobalListeners();
    activated = false;
    fireStateEvent(false);
    removeStores();
    onDeactivated();
    if (nonNull(routingTask)) {
      routingTask.enable();
    }
  }

  private void removeStores() {
    storeRegisterations.forEach(RegistrationHandler::remove);
    storeRegisterations.clear();
  }

  @Override
  protected void onActivated() {}

  @Override
  protected void onDeactivated() {}

  public void setState(DominoHistory.State state) {}

  public void registerStore(String key, IsStore<?> store) {
    RegistrationHandler registrationHandler = StoreRegistry.INSTANCE.registerStore(key, store);
    storeRegisterations.add(registrationHandler);
  }

  public void setRoutingTask(PresenterRoutingTask routingTask) {
    this.routingTask = routingTask;
  }

  public final void onSkippedRouting() {
    if (!routingTask.isEnabled()) {
      routingTask.enable();
    }
  }

  protected void publishState(String token, String title, String data) {
    routingTask.disable();
    history().fireState(token, title, data);
  }

  protected void publishState(
      String token, String title, String data, TokenParameter... parameters) {
    routingTask.disable();
    history().fireState(token, title, data, parameters);
  }

  protected void publishState(String token) {
    routingTask.disable();
    history().fireState(token);
  }

  protected void publishState(String token, TokenParameter... parameters) {
    routingTask.disable();
    history().fireState(token, parameters);
  }

  protected boolean isAutoActivate() {
    return true;
  }

  public final boolean isActivated() {
    return activated;
  }

  protected Map<Class<? extends DominoEvent>, DominoEventListener> getListeners() {
    return new HashMap<>();
  }

  protected Map<Class<? extends DominoEvent>, GlobalDominoEventListener> getGlobalListeners() {
    return new HashMap<>();
  }

  protected <E extends DominoEvent> void fireEvent(
      Class<E> extensionPointInterface, E extensionPoint) {
    DominoEvents.fire(extensionPointInterface, extensionPoint);
  }

  protected void runAsync(AsyncRunner.AsyncTask asyncTask) {
    ClientApp.make().getAsyncRunner().runAsync(asyncTask);
  }

  protected AppHistory history() {
    return ClientApp.make().getHistory();
  }
}
