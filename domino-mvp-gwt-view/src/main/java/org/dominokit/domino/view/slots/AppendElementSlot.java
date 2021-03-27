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

import elemental2.dom.HTMLElement;
import org.dominokit.domino.gwt.client.slots.ElementSlot;
import org.dominokit.domino.ui.utils.DominoElement;
import org.jboss.elemento.IsElement;

public class AppendElementSlot extends ElementSlot {

  private DominoElement<HTMLElement> element;

  public static AppendElementSlot of(HTMLElement element) {
    return new AppendElementSlot(element);
  }

  public static AppendElementSlot of(IsElement element) {
    return new AppendElementSlot(element);
  }

  public AppendElementSlot(HTMLElement element) {
    this.element = DominoElement.of(element);
  }

  public AppendElementSlot(DominoElement<HTMLElement> element) {
    this.element = element;
  }

  public AppendElementSlot(IsElement<HTMLElement> element) {
    this.element = DominoElement.of(element);
  }

  @Override
  public void updateContent(HTMLElement view) {
    DominoElement.of(element).appendChild(view);
  }
}
