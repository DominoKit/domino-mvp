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
package org.dominokit.domino.api.shared.store;

import java.util.Optional;

public abstract class AbstractStore<T> {
  private T data;

  public Optional<T> getData() {
    return Optional.ofNullable(data);
  }

  public void setData(T data) {
    this.data = data;
  }

  public void setData(T data, DataHandler<T> onUpdate) {
    this.data = data;
    onUpdate.onDataUpdated(this.data);
  }

  public interface DataHandler<T> {
    void onDataUpdated(T data);
  }
}
