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
import jsinterop.base.Js;
import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.gwt.client.slots.ElementSlot;
import org.jboss.elemento.IsElement;

public class AppendElementSlot extends ElementSlot {

  public static final String APPEND_ELEMENT_SLOT = "append-element-slot";
  private Element element;

  public static AppendElementSlot of(Element element) {
    return new AppendElementSlot(element);
  }

  public static AppendElementSlot of(IsElement<? extends Element> element) {
    return new AppendElementSlot(element.element());
  }

  public AppendElementSlot(Element element) {
    this.element = element;
  }

  public AppendElementSlot(IsElement<HTMLElement> element) {
    this.element = Js.<HTMLElement>uncheckedCast(element.element());
  }

  @Override
  public void setType() {
    this.element.setAttribute(IsSlot.DOMINO_SLOT_TYPE, APPEND_ELEMENT_SLOT);
  }

  @Override
  public void cleanUp() {
    this.element.removeAttribute(IsSlot.DOMINO_SLOT_NAME);
    this.element.removeAttribute(IsSlot.DOMINO_SLOT_TYPE);
  }

  @Override
  public void updateContent(Element view) {
    element.appendChild(view);
  }

  @Override
  protected Element getElement() {
    return element;
  }
}
