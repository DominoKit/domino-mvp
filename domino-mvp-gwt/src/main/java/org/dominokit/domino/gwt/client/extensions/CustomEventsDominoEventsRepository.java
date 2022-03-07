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
package org.dominokit.domino.gwt.client.extensions;

import static java.util.Objects.isNull;

import elemental2.dom.*;
import elemental2.dom.EventListener;
import java.util.*;
import java.util.stream.Collectors;
import jsinterop.base.Js;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.extension.DominoEventsListenersRepository;
import org.dominokit.domino.api.shared.extension.DominoEvent;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.extension.GlobalDominoEventListener;
import org.dominokit.domino.api.shared.extension.GlobalEvent;

public class CustomEventsDominoEventsRepository implements DominoEventsListenersRepository {

  private final Map<String, List<ListenerWrapper>> listeners = new HashMap<>();
  private final Map<String, List<GlobalListenerWrapper>> globalListeners = new HashMap<>();

  @Override
  public void addListener(
      Class<? extends DominoEvent> dominoEvent, DominoEventListener dominoEventListener) {
    initializeEventListeners(dominoEvent);
    ListenerWrapper wrapper = new ListenerWrapper(dominoEventListener);
    listeners.get(dominoEvent.getCanonicalName()).add(wrapper);
  }

  @Override
  public void addGlobalListener(
      Class<? extends DominoEvent> dominoEvent, GlobalDominoEventListener dominoEventListener) {
    initializeGlobalEventListeners(dominoEvent);
    GlobalListenerWrapper wrapper = new GlobalListenerWrapper(dominoEventListener);
    globalListeners.get(dominoEvent.getCanonicalName()).add(wrapper);
    DomGlobal.document.addEventListener(
        dominoEvent.getCanonicalName(), wrapper.dominoCustomEventListener);
  }

  @Override
  public Set<DominoEventListener> getEventListeners(Class<? extends DominoEvent> dominoEvent) {
    initializeEventListeners(dominoEvent);
    return listeners.get(dominoEvent.getCanonicalName()).stream()
        .map(cw -> cw.dominoEventListener)
        .collect(Collectors.toSet());
  }

  @Override
  public void fireEvent(Class<? extends DominoEvent> eventType, DominoEvent dominoEvent) {
    if (dominoEvent instanceof GlobalEvent) {
      CustomEventInit customEventInit = CustomEventInit.create();
      customEventInit.setDetail(((GlobalEvent) dominoEvent).serialize());
      CustomEvent customEvent = new CustomEvent(eventType.getCanonicalName(), customEventInit);
      DomGlobal.document.dispatchEvent(customEvent);
    } else {
      getEventListeners(eventType)
          .forEach(
              listener ->
                  ClientApp.make()
                      .getAsyncRunner()
                      .runAsync(() -> listener.onEventReceived(dominoEvent)));
    }
  }

  private List<ListenerWrapper> getEventListenersWrapper(Class<? extends DominoEvent> dominoEvent) {
    initializeEventListeners(dominoEvent);
    return listeners.get(dominoEvent.getCanonicalName());
  }

  private List<GlobalListenerWrapper> getGlobalEventListenersWrapper(
      Class<? extends DominoEvent> dominoEvent) {
    initializeGlobalEventListeners(dominoEvent);
    return globalListeners.get(dominoEvent.getCanonicalName());
  }

  @Override
  public void removeListener(Class<? extends DominoEvent> event, DominoEventListener listener) {
    List<ListenerWrapper> eventListeners = getEventListenersWrapper(event);
    eventListeners.remove(new ListenerWrapper(listener));

    if (eventListeners.isEmpty()) {
      listeners.remove(event.getCanonicalName());
    }
  }

  @Override
  public void removeGlobalListener(
      Class<? extends DominoEvent> event, GlobalDominoEventListener listener) {
    List<GlobalListenerWrapper> eventListeners = getGlobalEventListenersWrapper(event);
    int index = eventListeners.indexOf(new GlobalListenerWrapper(listener));
    if (index >= 0) {
      GlobalListenerWrapper tobeRemoved = eventListeners.get(index);
      eventListeners.remove(tobeRemoved);
      DomGlobal.document.removeEventListener(
          event.getCanonicalName(), tobeRemoved.dominoCustomEventListener);
      DomGlobal.console.info(
          "Event listener removed : "
              + event.getCanonicalName()
              + " : "
              + listener.getClass().getCanonicalName());
    }

    if (eventListeners.isEmpty()) {
      globalListeners.remove(event.getCanonicalName());
    }
  }

  private void initializeEventListeners(Class<? extends DominoEvent> extensionPoint) {
    if (isNull(listeners.get(extensionPoint.getCanonicalName())))
      listeners.put(extensionPoint.getCanonicalName(), new LinkedList<>());
  }

  private void initializeGlobalEventListeners(Class<? extends DominoEvent> extensionPoint) {
    if (isNull(globalListeners.get(extensionPoint.getCanonicalName())))
      globalListeners.put(extensionPoint.getCanonicalName(), new LinkedList<>());
  }

  public static class DominoCustomEventListener implements EventListener {

    private final DominoEventListener<DominoEvent> dominoEventListener;

    public DominoCustomEventListener(DominoEventListener<DominoEvent> dominoEventListener) {
      this.dominoEventListener = dominoEventListener;
    }

    @Override
    public void handleEvent(Event evt) {
      CustomEvent customEvent = Js.uncheckedCast(evt);
      if (dominoEventListener instanceof GlobalDominoEventListener) {
        DominoEvent event =
            ((GlobalDominoEventListener<?>) dominoEventListener)
                .deserializeEvent((String) customEvent.detail);
        dominoEventListener.onEventReceived(event);
      }
    }
  }

  private static class GlobalListenerWrapper extends ListenerWrapper {
    private final DominoCustomEventListener dominoCustomEventListener;

    public GlobalListenerWrapper(DominoEventListener<DominoEvent> dominoEventListener) {
      super(dominoEventListener);
      this.dominoCustomEventListener = new DominoCustomEventListener(dominoEventListener);
    }
  }

  private static class ListenerWrapper {

    private final DominoEventListener<DominoEvent> dominoEventListener;

    public ListenerWrapper(DominoEventListener<DominoEvent> dominoEventListener) {
      this.dominoEventListener = dominoEventListener;
    }

    @Override
    public boolean equals(Object other) {
      if (isNull(other)) return false;
      return dominoEventListener
          .getClass()
          .getCanonicalName()
          .equals(((ListenerWrapper) other).dominoEventListener.getClass().getCanonicalName());
    }

    @Override
    public int hashCode() {
      return dominoEventListener.getClass().getCanonicalName().hashCode();
    }
  }
}
