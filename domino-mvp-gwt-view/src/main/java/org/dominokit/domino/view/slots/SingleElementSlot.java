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

import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.gwt.client.slots.ElementSlot;
import org.dominokit.domino.ui.utils.ElementUtil;
import org.jboss.elemento.IsElement;

public class SingleElementSlot extends ElementSlot {

  public static final String SINGLE_ELEMENT_SLOT = "single-element-slot";
  private Element element;

  public static SingleElementSlot of(Element element) {
    return new SingleElementSlot(element);
  }

  public static SingleElementSlot of(IsElement<? extends Element> element) {
    return new SingleElementSlot(element.element());
  }

  public SingleElementSlot(Element element) {
    this.element = element;
  }

  public SingleElementSlot(IsElement<HTMLElement> element) {
    this.element = element.element();
  }

  @Override
  public void updateContent(Element view) {
    ElementUtil.clear(element);
    element.appendChild(view);
  }

  @Override
  public void setType() {
    this.element.setAttribute(IsSlot.DOMINO_SLOT_TYPE, SINGLE_ELEMENT_SLOT);
  }

  @Override
  public void cleanUp() {
    this.element.removeAttribute(IsSlot.DOMINO_SLOT_NAME);
    this.element.removeAttribute(IsSlot.DOMINO_SLOT_TYPE);
  }

  @Override
  protected Element getElement() {
    return element;
  }
}
