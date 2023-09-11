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
package org.dominokit.domino.api.client.mvp.presenter;

import java.util.function.Supplier;
import org.dominokit.domino.api.client.mvp.view.View;

public class ViewablePresenterSupplier<P extends ViewablePresenter<V>, V extends View>
    extends PresenterSupplier<P> {

  private Supplier<V> viewSupplier;

  public ViewablePresenterSupplier(boolean singleton, Supplier<P> presenterFactory) {
    super(singleton, presenterFactory);
  }

  @Override
  protected void onBeforeInitPresenter() {
    presenter.setViewSupplier(viewSupplier);
  }

  public void setViewSupplier(Supplier<V> viewSupplier) {
    this.viewSupplier = viewSupplier;
  }
}
