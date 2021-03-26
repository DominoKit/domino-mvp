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
package org.dominokit.domino.view.slots;

import static java.util.Objects.nonNull;

import elemental2.dom.HTMLBodyElement;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.dominokit.domino.api.client.mvp.slots.ContentSlot;
import org.dominokit.domino.api.client.mvp.view.HasContent;
import org.dominokit.domino.api.shared.extension.Content;
import org.dominokit.domino.ui.utils.DominoElement;

public class BodyElementSlot implements ContentSlot {

  private DominoElement<HTMLBodyElement> body = DominoElement.body();

  private static final BodyElementSlot INSTANCE = new BodyElementSlot();

  private Content currentContent;

  public static BodyElementSlot create() {
    return INSTANCE;
  }

  private BodyElementSlot() {}

  @Override
  public void updateContent(Content view) {
    if (nonNull(currentContent)) {
      HTMLElement contentElement = Js.uncheckedCast(currentContent.get());
      DominoElement.of(contentElement).remove();
    }
    body.appendChild(Js.<HTMLElement>uncheckedCast(view.get()));
    currentContent = view;
  }

  @Override
  public void updateContent(HasContent view, HasContent.CreateHandler createHandler) {
    updateContent(view.getContent(createHandler));
  }
}
