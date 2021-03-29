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
package org.dominokit.domino.view;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.shared.extension.PredefinedSlots;
import org.dominokit.domino.view.slots.BodyElementSlot;
import org.dominokit.domino.view.slots.ModalSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GwtView {
  public static final Logger LOGGER = LoggerFactory.getLogger(GwtView.class);

  public static void init() {
    LOGGER.info("[body-slot] slot registered");
    ClientApp.make()
        .slotsManager()
        .registerSlot(PredefinedSlots.BODY_SLOT, BodyElementSlot.create());
    ClientApp.make().slotsManager().registerSlot(PredefinedSlots.MODAL_SLOT, ModalSlot.create());
  }
}
