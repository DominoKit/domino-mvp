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

import jsinterop.base.Js;
import org.dominokit.domino.api.client.mvp.slots.ContentSlot;
import org.dominokit.domino.api.client.mvp.view.HasContent;
import org.dominokit.domino.api.client.mvp.view.ModalView;
import org.dominokit.domino.api.shared.extension.Content;

public class ModalSlot implements ContentSlot {

  public static ModalSlot create() {
    return new ModalSlot();
  }

  @Override
  public void updateContent(HasContent view, HasContent.CreateHandler createHandler) {
    view.getContent(createHandler);
    ModalView modalView = Js.uncheckedCast(view);
    modalView.open();
  }

  @Override
  public void updateContent(Content view) {}
}
