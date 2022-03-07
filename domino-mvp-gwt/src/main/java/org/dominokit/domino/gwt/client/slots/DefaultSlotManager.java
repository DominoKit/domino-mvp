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

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.dominokit.domino.api.client.mvp.slots.ContentSlot;
import org.dominokit.domino.api.client.mvp.slots.InvalidSlotException;
import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.api.client.mvp.slots.SlotsManager;
import org.dominokit.domino.api.client.mvp.view.HasContent;

public class DefaultSlotManager implements SlotsManager {

  private static final Map<String, Deque<IsSlot<?>>> SLOTS = new HashMap<>();

  @Override
  public void registerSlot(String key, IsSlot<?> slot) {
    if (!SLOTS.containsKey(key.toLowerCase())) {
      SLOTS.put(key.toLowerCase(), new LinkedList<>());
    }
    SLOTS.get(key.toLowerCase()).push(slot);
  }

  @Override
  public void removeSlot(String key) {
    if (SLOTS.containsKey(key.toLowerCase())) {
      IsSlot<?> slot = SLOTS.get(key.toLowerCase()).pop();
      slot.cleanUp();
    }
  }

  @Override
  public void revealView(String slotKey, HasContent view, HasContent.CreateHandler createHandler) {
    if (!SLOTS.containsKey(slotKey)) {
      throw new InvalidSlotException(slotKey);
    }
    if (SLOTS.containsKey(slotKey.toLowerCase())) {
      ContentSlot slot = (ContentSlot) SLOTS.get(slotKey.toLowerCase()).peek();
      slot.updateContent(view, createHandler);
    }
  }
}
