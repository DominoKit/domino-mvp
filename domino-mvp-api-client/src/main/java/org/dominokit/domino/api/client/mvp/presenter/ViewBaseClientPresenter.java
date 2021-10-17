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

import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.mvp.slots.*;
import org.dominokit.domino.api.client.mvp.view.*;

public abstract class ViewBaseClientPresenter<V extends View> extends BaseClientPresenter {

  public static final Logger LOGGER = Logger.getLogger(ViewBaseClientPresenter.class.getName());

  protected V view;
  private Supplier<V> viewSupplier;

  @Override
  protected void initialize() {
    view = viewSupplier.get();
    if (view instanceof DominoView) {
      ((DominoView) view).setRevealHandler(getViewRevealHandler());
      ((DominoView) view).setRemoveHandler(getViewRemoveHandler());
    }
    if (this instanceof UiHandlers && view instanceof HasUiHandlers) {
      ((HasUiHandlers) view).setUiHandlers((UiHandlers) this);
    }
    super.initialize();
  }

  public String revealSlot() {
    return null;
  }

  public void revealInSlot(String key) {
    try {
      if (view instanceof HasContent) {
        onBeforeReveal();
        ClientApp.make().slotsManager().revealView(key, (HasContent) view, this::registerSlots);
      } else {
        throw new RevealViewWithNoContentException(view.getClass().getCanonicalName());
      }
    } catch (InvalidSlotException e) {
      LOGGER.log(
          Level.SEVERE,
          "Slot ["
              + key
              + "] not found, required by presenter : ["
              + this.getClass().getCanonicalName()
              + "]");
    }
  }

  public void reveal() {
    if (nonNull(revealSlot()) && !revealSlot().trim().isEmpty()) {
      revealInSlot(revealSlot());
    }
  }

  protected void onBeforeReveal() {}

  private DominoView.RevealedHandler getViewRevealHandler() {
    return () -> {
      RevealedHandler revealHandler = getRevealHandler();
      if (nonNull(revealHandler)) {
        revealHandler.onRevealed();
      }
      activate();
    };
  }

  @Override
  protected void registerByName() {
    // do nothing presenter will be registered after slots are registered.
  }

  private void registerSlots() {
    SlotsEntries slotsEntries = getSlots();
    if (nonNull(slotsEntries)) {
      slotsEntries
          .getSlots()
          .forEach(
              (key, slot) -> {
                LOGGER.info(
                    "Presenter ["
                        + this.getClass().getCanonicalName()
                        + "] is registering slot ["
                        + key
                        + "]");
                ClientApp.make().slotsManager().registerSlot(key, slot);
              });
    }

    getName().ifPresent(NamedPresenters::registerPresenter);
  }

  @Override
  public Optional<String> getName() {
    return Optional.empty();
  }

  @Override
  public Optional<String> getParent() {
    return Optional.empty();
  }

  protected SlotsEntries getSlots() {
    return SlotsEntries.create();
  }

  @Override
  protected void fireActivationEvent(boolean stats) {}

  @Override
  protected final boolean isAutoActivate() {
    return false;
  }

  private DominoView.RemovedHandler getViewRemoveHandler() {
    return () -> {
      SlotsEntries slotsEntries = getSlots();
      if (nonNull(slotsEntries)) {
        slotsEntries
            .getSlots()
            .forEach(
                (key, slot) -> {
                  LOGGER.info(
                      "Presenter ["
                          + this.getClass().getCanonicalName()
                          + "] is removing slot ["
                          + key
                          + "]");
                  ClientApp.make().slotsManager().removeSlot(key);
                });
      }
      RemovedHandler removeHandler = getRemoveHandler();
      if (nonNull(removeHandler)) {
        removeHandler.onRemoved();
      }
      deActivate();
      getName().ifPresent(NamedPresenters::removePresenter);
    };
  }

  protected RevealedHandler getRevealHandler() {
    return null;
  }

  protected RemovedHandler getRemoveHandler() {
    return null;
  }

  public void setViewSupplier(Supplier<V> viewSupplier) {
    this.viewSupplier = viewSupplier;
  }

  @FunctionalInterface
  public interface RevealedHandler {
    void onRevealed();
  }

  @FunctionalInterface
  public interface RemovedHandler {
    void onRemoved();
  }
}
