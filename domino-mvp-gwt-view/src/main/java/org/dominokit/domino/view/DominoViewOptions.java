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

import org.dominokit.domino.api.client.InitOptions;
import org.dominokit.domino.api.client.mvp.slots.SlotsManager;
import org.dominokit.domino.api.shared.extension.PredefinedSlots;
import org.dominokit.domino.view.slots.BodyElementSlot;
import org.dominokit.domino.view.slots.ElementsSlotsManager;
import org.dominokit.domino.view.slots.ModalSlot;
import org.dominokit.domino.view.slots.NoContentSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DominoViewOptions implements InitOptions {
  public static final Logger LOGGER = LoggerFactory.getLogger(DominoViewOptions.class);
  private String rootPath = "";
  private SlotsManager slotsManager;

  public static DominoViewOptions getInstance() {
    return new DominoViewOptions();
  }

  public DominoViewOptions() {
    this.slotsManager = new ElementsSlotsManager();
    this.slotsManager.registerSlot(PredefinedSlots.BODY_SLOT, BodyElementSlot.create());
    LOGGER.info("[" + PredefinedSlots.BODY_SLOT + "] slot registered");
    this.slotsManager.registerSlot(PredefinedSlots.MODAL_SLOT, ModalSlot.create());
    LOGGER.info("[" + PredefinedSlots.MODAL_SLOT + "] slot registered");
    this.slotsManager.registerSlot(PredefinedSlots.NO_CONTENT_SLOT, NoContentSlot.create());
    LOGGER.info("[" + PredefinedSlots.NO_CONTENT_SLOT + "] slot registered");
  }

  public DominoViewOptions setRootPath(String rootPath) {
    this.rootPath = rootPath;
    return this;
  }

  @Override
  public String getRootPath() {
    return rootPath;
  }

  @Override
  public SlotsManager getSlotsManager() {
    return slotsManager;
  }
}
