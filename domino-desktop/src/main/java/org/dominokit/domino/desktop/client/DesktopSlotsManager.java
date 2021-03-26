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
package org.dominokit.domino.desktop.client;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;
import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.api.client.mvp.slots.SlotsManager;
import org.dominokit.domino.api.client.mvp.view.HasContent;

public class DesktopSlotsManager implements SlotsManager {
  public static final Logger LOGGER = Logger.getLogger(DesktopSlotsManager.class.getName());

  private static final Map<String, Deque<IsSlot>> SLOT_QUEUE = new HashMap<>();

  public void registerSlot(String key, IsSlot slot) {
    LOGGER.info(" >> REGISTERING SLOT [" + key + "]");
    if (!SLOT_QUEUE.containsKey(key.toLowerCase())) {
      SLOT_QUEUE.put(key.toLowerCase(), new LinkedList<>());
    }

    SLOT_QUEUE.get(key.toLowerCase()).push(slot);
  }

  public void removeSlot(String key) {
    LOGGER.info(" << REMOVING SLOT [" + key + "]");
    if (SLOT_QUEUE.containsKey(key.toLowerCase())) {
      IsSlot popedOut = SLOT_QUEUE.get(key.toLowerCase()).pop();
      popedOut.cleanUp();
    }
  }

  public static IsSlot get(String key) {
    return SLOT_QUEUE.get(key.toLowerCase()).peek();
  }

  @Override
  public void revealView(String slotKey, HasContent view, HasContent.CreateHandler createHandler) {
    get(slotKey).updateContent(view.getContent());
  }
}
