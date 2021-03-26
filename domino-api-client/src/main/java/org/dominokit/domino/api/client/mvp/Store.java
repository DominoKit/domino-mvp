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
package org.dominokit.domino.api.client.mvp;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Store<T> implements IsStore<T> {

  private T data;
  private List<Consumer<T>> consumers = new ArrayList<>();
  private boolean removeWhenConsumed = false;

  public Store() {}

  public Store(T data) {
    this.data = data;
  }

  public Store(T data, boolean removeWhenConsumed) {
    this.data = data;
    this.removeWhenConsumed = removeWhenConsumed;
  }

  public boolean isRemoveWhenConsumed() {
    return removeWhenConsumed;
  }

  public void updateData(T data) {
    this.data = data;
    consumers.forEach(consumer -> consumer.accept(this.data));
  }

  @Override
  public T getData() {
    if (removeWhenConsumed) {
      T temp = data;
      reset();
      return temp;
    }
    return data;
  }

  @Override
  public RegistrationHandler consumeData(Consumer<T> consumer) {
    consumers.add(consumer);
    if (nonNull(data)) {
      consumer.accept(data);
      if (removeWhenConsumed) {
        reset();
      }
    }
    return () -> consumers.remove(consumer);
  }

  @Override
  public boolean hasData() {
    return nonNull(data);
  }

  public void reset() {
    consumers.clear();
    this.data = null;
  }
}
