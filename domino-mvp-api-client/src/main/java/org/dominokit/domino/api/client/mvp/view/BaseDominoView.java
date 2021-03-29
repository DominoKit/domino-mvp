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
package org.dominokit.domino.api.client.mvp.view;

import static java.util.Objects.nonNull;

import org.dominokit.domino.api.shared.extension.Content;

public abstract class BaseDominoView<T> implements DominoView<T>, HasContent {

  private boolean initialized = false;
  protected RevealedHandler revealHandler;
  protected RemovedHandler removeHandler;

  private T root;

  @Override
  public boolean isInitialized() {
    return initialized;
  }

  @Override
  public void setInitialized(boolean initialized) {
    this.initialized = initialized;
  }

  @Override
  public boolean isSingleton() {
    return false;
  }

  @Override
  public Content getContent() {
    return this.getContent(null);
  }

  @Override
  public Content getContent(CreateHandler createHandler) {
    if (!initialized || !isSingleton()) {
      root = init();
      initRoot(root);
      if (nonNull(createHandler)) {
        createHandler.onCreated();
      }
      initialized = true;
    }
    return (Content<T>) () -> root;
  }

  protected abstract void initRoot(T root);

  protected abstract T init();

  @Override
  public void clear() {}

  @Override
  public void setRevealHandler(RevealedHandler revealHandler) {
    this.revealHandler = revealHandler;
  }

  @Override
  public void setRemoveHandler(RemovedHandler removeHandler) {
    this.removeHandler = removeHandler;
  }
}
