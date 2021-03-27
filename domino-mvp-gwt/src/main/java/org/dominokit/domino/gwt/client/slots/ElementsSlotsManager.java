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
package org.dominokit.domino.gwt.client.slots;

import elemental2.dom.*;
import elemental2.dom.EventListener;
import java.util.*;
import java.util.logging.Logger;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.dominokit.domino.api.client.mvp.slots.ContentSlot;
import org.dominokit.domino.api.client.mvp.slots.InvalidSlotException;
import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.api.client.mvp.slots.SlotsManager;
import org.dominokit.domino.api.client.mvp.view.HasContent;

public class ElementsSlotsManager implements SlotsManager {

  public static final Logger LOGGER = Logger.getLogger(ElementsSlotsManager.class.getName());

  private static final Map<String, Deque<EventListener>> LISTENERS_SLOTS = new HashMap<>();
  private static final Map<String, Deque<IsSlot>> NORMAL_SLOTS = new HashMap<>();

  public ElementsSlotsManager() {
    DomGlobal.document.addEventListener(
        "domino-slot-added",
        evt -> {
          CustomEvent addEvent = Js.uncheckedCast(evt);
          JsPropertyMap<Object> eventDetails = Js.uncheckedCast(addEvent.detail);
          String slotKey = Js.uncheckedCast(eventDetails.get("slot-key"));
          EventListener slotListener = Js.uncheckedCast(eventDetails.get("slot-listener"));
          initSlotQueueIfNotExists(slotKey);
          if (!LISTENERS_SLOTS.get(slotKey).isEmpty()) {
            EventListener currentListener = LISTENERS_SLOTS.get(slotKey).peek();
            DomGlobal.document.removeEventListener(slotKey + "-slot-event", currentListener);
          }
          LISTENERS_SLOTS.get(slotKey).push(slotListener);
          DomGlobal.document.addEventListener(slotKey + "-slot-event", slotListener);
        });

    DomGlobal.document.addEventListener(
        "domino-slot-removed",
        evt -> {
          CustomEvent addEvent = Js.uncheckedCast(evt);
          JsPropertyMap<Object> eventDetails = Js.uncheckedCast(addEvent.detail);
          String slotKey = Js.uncheckedCast(eventDetails.get("slot-key"));
          EventListener slotListener = Js.uncheckedCast(eventDetails.get("slot-listener"));

          LISTENERS_SLOTS.get(slotKey).remove(slotListener);
          DomGlobal.document.removeEventListener(slotKey + "-slot-event", slotListener);

          if (!LISTENERS_SLOTS.get(slotKey).isEmpty()) {
            EventListener replacementListener = LISTENERS_SLOTS.get(slotKey).peek();
            DomGlobal.document.removeEventListener(slotKey + "-slot-event", replacementListener);
          }
        });
  }

  private void initSlotQueueIfNotExists(String slotKey) {
    if (!LISTENERS_SLOTS.containsKey(slotKey)) {
      LISTENERS_SLOTS.put(slotKey, new LinkedList<>());
    }
  }

  public void registerSlot(String key, IsSlot slot) {

    if (slot instanceof ElementSlot) {
      LOGGER.info(" >> REGISTERING GLOBAL SLOT [" + key + "]");
      if (!LISTENERS_SLOTS.containsKey(key.toLowerCase())) {
        LISTENERS_SLOTS.put(key.toLowerCase(), new LinkedList<>());
      }
      DomGlobal.document.dispatchEvent(slotEvent("domino-slot-added", key, Js.uncheckedCast(slot)));
    } else {
      LOGGER.info(" >> REGISTERING SLOT [" + key + "]");
      if (!NORMAL_SLOTS.containsKey(key.toLowerCase())) {
        NORMAL_SLOTS.put(key.toLowerCase(), new LinkedList<>());
      }
      NORMAL_SLOTS.get(key.toLowerCase()).push(slot);
    }
  }

  public void removeSlot(String key) {
    LOGGER.info(" << REMOVING SLOT [" + key + "]");
    if (LISTENERS_SLOTS.containsKey(key.toLowerCase())) {
      EventListener poppedOut = LISTENERS_SLOTS.get(key.toLowerCase()).pop();
      DomGlobal.document.dispatchEvent(slotEvent("domino-slot-removed", key, poppedOut));
    } else {
      NORMAL_SLOTS.get(key.toLowerCase()).pop();
    }
  }

  @Override
  public void revealView(String slotKey, HasContent view, HasContent.CreateHandler createHandler) {
    if (!LISTENERS_SLOTS.containsKey(slotKey) && !NORMAL_SLOTS.containsKey(slotKey)) {
      throw new InvalidSlotException(slotKey);
    }
    if (LISTENERS_SLOTS.containsKey(slotKey)) {
      HTMLElement viewElement = Js.uncheckedCast(view.getContent(createHandler).get());
      CustomEventInit slotEventDetails = CustomEventInit.create();
      slotEventDetails.setDetail(viewElement);

      CustomEvent slotEvent = new CustomEvent(slotKey + "-slot-event", slotEventDetails);
      DomGlobal.document.dispatchEvent(slotEvent);
    } else {
      ContentSlot slot = (ContentSlot) NORMAL_SLOTS.get(slotKey).peek();
      slot.updateContent(view, createHandler);
    }
  }

  private CustomEvent slotEvent(String eventType, String key, EventListener eventListener) {
    JsPropertyMap<Object> eventDetails = JsPropertyMap.of();
    eventDetails.set("slot-key", key);
    eventDetails.set("slot-listener", eventListener);

    CustomEventInit customEventInit = CustomEventInit.create();
    customEventInit.setDetail(eventDetails);

    return new CustomEvent(eventType, customEventInit);
  }
}
