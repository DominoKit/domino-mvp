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
package org.dominokit.domino.view.slots;

import static java.util.Objects.nonNull;
import static org.dominokit.domino.view.slots.AppendElementSlot.APPEND_ELEMENT_SLOT;
import static org.dominokit.domino.view.slots.SingleElementSlot.SINGLE_ELEMENT_SLOT;

import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import jsinterop.base.Js;
import org.dominokit.domino.api.client.mvp.slots.ContentSlot;
import org.dominokit.domino.api.client.mvp.slots.InvalidSlotException;
import org.dominokit.domino.api.client.mvp.slots.InvalidSlotTypeException;
import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.api.client.mvp.slots.SlotsManager;
import org.dominokit.domino.api.client.mvp.view.HasContent;
import org.dominokit.domino.gwt.client.slots.ElementSlot;
import org.dominokit.domino.ui.utils.ElementsFactory;

public class ElementsSlotsManager implements SlotsManager, ElementsFactory {

  public static final Logger LOGGER = Logger.getLogger(ElementsSlotsManager.class.getName());

  private static final Map<String, Deque<IsSlot<?>>> PREDEFINED_SLOTS = new HashMap<>();

  private Optional<HTMLElement> getSlotElementByKey(String slotKey) {
    return Optional.ofNullable(getSlotElementByKey(slotKey, DomGlobal.document.body));
  }

  private HTMLElement getSlotElementByKey(String slotKey, HTMLElement parent) {
    HTMLElement element =
        Js.uncheckedCast(parent.querySelector("[" + IsSlot.DOMINO_SLOT_NAME + "=" + slotKey + "]"));
    if (nonNull(element)) {
      return Optional.ofNullable(getSlotElementByKey(slotKey, element)).orElse(element);
    }
    return null;
  }

  public void registerSlot(String key, IsSlot<?> slot) {
    LOGGER.info(" >> REGISTERING SLOT [" + key + "]");
    slot.setName(key);
    slot.setType();
    if (slot instanceof ElementSlot) {
      return;
    }
    if (!PREDEFINED_SLOTS.containsKey(key.toLowerCase())) {
      PREDEFINED_SLOTS.put(key.toLowerCase(), new LinkedList<>());
    }
    PREDEFINED_SLOTS.get(key.toLowerCase()).push(slot);
  }

  public void removeSlot(String key) {
    LOGGER.info(" << REMOVING SLOT [" + key + "]");
    if (PREDEFINED_SLOTS.containsKey(key.toLowerCase())) {
      IsSlot<?> slot = PREDEFINED_SLOTS.get(key.toLowerCase()).pop();
      slot.cleanUp();

    } else {
      Optional<HTMLElement> elementSlot = getSlotElementByKey(key);
      elementSlot.ifPresent(
          element -> {
            String slotType = getSlotType(element);
            Optional<ElementSlot> elementSlotByType = getElementSlotByType(slotType, element);
            elementSlotByType.ifPresent(IsSlot::cleanUp);
          });
    }
  }

  private String getSlotType(HTMLElement element) {
    return elementOf(element).getAttribute(IsSlot.DOMINO_SLOT_TYPE);
  }

  @Override
  public void revealView(String slotKey, HasContent view, HasContent.CreateHandler createHandler) {
    if (!PREDEFINED_SLOTS.containsKey(slotKey) && !getSlotElementByKey(slotKey).isPresent()) {
      throw new InvalidSlotException(slotKey);
    }
    if (PREDEFINED_SLOTS.containsKey(slotKey.toLowerCase())) {
      ContentSlot slot = (ContentSlot) PREDEFINED_SLOTS.get(slotKey.toLowerCase()).peek();
      slot.updateContent(view, createHandler);
    } else {
      Optional<HTMLElement> slotElement = getSlotElementByKey(slotKey);
      if (slotElement.isPresent()) {
        Optional<ElementSlot> elementSlotByType =
            getElementSlotByType(getSlotType(slotElement.get()), slotElement.get());
        HTMLElement viewElement = Js.uncheckedCast(view.getContent(createHandler).get());
        elementSlotByType.get().updateContent(viewElement);
      } else {
        throw new InvalidSlotException(slotKey);
      }
    }
  }

  public Optional<ElementSlot> getElementSlotByType(String type, HTMLElement element) {
    switch (type) {
      case SINGLE_ELEMENT_SLOT:
        return Optional.of(SingleElementSlot.of(element));
      case APPEND_ELEMENT_SLOT:
        return Optional.of(AppendElementSlot.of(element));
      default:
        throw new InvalidSlotTypeException(type);
    }
  }
}
