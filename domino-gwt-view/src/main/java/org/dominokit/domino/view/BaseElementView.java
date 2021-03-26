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
package org.dominokit.domino.view;

import static java.util.Objects.nonNull;

import elemental2.dom.HTMLElement;
import org.dominokit.domino.api.client.mvp.view.BaseDominoView;
import org.dominokit.domino.ui.utils.ElementUtil;

public abstract class BaseElementView<T extends HTMLElement> extends BaseDominoView<T> {
  @Override
  protected final void initRoot(T root) {
    if (nonNull(root)) {
      ElementUtil.onAttach(
          root,
          mutationRecord -> {
            revealHandler.onRevealed();
          });

      ElementUtil.onDetach(
          root,
          mutationRecord -> {
            removeHandler.onRemoved();
            clear();
          });
    }
  }
}
