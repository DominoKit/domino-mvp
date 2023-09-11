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

import elemental2.dom.DomGlobal;
import elemental2.dom.Element;
import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.gwt.client.slots.ElementSlot;
import org.dominokit.domino.ui.utils.ElementsFactory;

public class ElementIdSlot extends ElementSlot implements ElementsFactory {

  public static final String ELEMENT_ID_SLOT = "single-element-slot";
  private final String id;
  private String name;

  public static ElementIdSlot of(String id) {
    return new ElementIdSlot(id);
  }

  public ElementIdSlot(String id) {
    this.id = id;
  }

  @Override
  public void updateContent(Element view) {
    elementOf(DomGlobal.document.getElementById(id)).clearElement().appendChild(view);
  }

  @Override
  public void setType() {
    elementOf(DomGlobal.document.getElementById(id))
        .setAttribute(IsSlot.DOMINO_SLOT_TYPE, ELEMENT_ID_SLOT);
  }

  @Override
  public void cleanUp() {
    elementOf(DomGlobal.document.getElementById(id)).removeAttribute(IsSlot.DOMINO_SLOT_NAME);
    elementOf(DomGlobal.document.getElementById(id)).removeAttribute(IsSlot.DOMINO_SLOT_TYPE);
  }

  @Override
  protected Element getElement() {
    return DomGlobal.document.getElementById(id);
  }
}
