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
package org.dominokit.domino.api.client.mvp.presenter;

import static java.util.Objects.isNull;

import java.util.function.Supplier;

public class PresenterSupplier<P extends Presentable> implements Supplier<P> {

  protected P presenter;
  protected final boolean singleton;
  protected Supplier<P> presenterFactory;

  public PresenterSupplier(boolean singleton, Supplier<P> presenterFactory) {
    this.singleton = singleton;
    this.presenterFactory = presenterFactory;
  }

  @Override
  public P get() {
    if (isNull(presenter) || !singleton) {
      presenter = presenterFactory.get();
      onBeforeInitPresenter();
      presenter.init();
    }
    return presenter;
  }

  protected void onBeforeInitPresenter() {}
}
