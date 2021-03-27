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
package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.mvp.view.BaseDominoView;

public abstract class FakeView extends BaseDominoView<FakeElement> {

  private boolean revealed = false;

  @Override
  protected void initRoot(FakeElement root) {}

  @Override
  protected FakeElement init() {
    return new FakeElement(
        attached -> {
          if (attached) {
            revealHandler.onRevealed();
            revealed = true;
          } else {
            removeHandler.onRemoved();
            revealed = false;
          }
        });
  }

  public boolean isRevealed() {
    return revealed;
  }
}
