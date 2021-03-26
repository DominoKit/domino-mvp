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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class StoreRegistry {
  public static final StoreRegistry INSTANCE = new StoreRegistry();

  private Map<String, IsStore<?>> stores = new HashMap<>();
  private Map<String, List<Consumer<IsStore<?>>>> consumers = new HashMap<>();

  private StoreRegistry() {}

  public RegistrationHandler registerStore(String key, IsStore<?> store) {
    stores.put(key, store);
    if (consumers.containsKey(key)) {
      consumers.get(key).forEach(consumer -> consumer.accept(store));
    }

    return () -> {
      stores.remove(key);
      consumers.remove(key);
    };
  }

  private void addConsumer(String key, Consumer<IsStore<?>> consumer) {
    if (!consumers.containsKey(key)) {
      consumers.put(key, new ArrayList<>());
    }
    consumers.get(key).add(consumer);
  }

  public <T> IsStore<T> getStore(String key) {
    return (IsStore<T>) stores.get(key);
  }

  public <T> RegistrationHandler consumeStore(String key, Consumer<IsStore<?>> consumer) {
    addConsumer(key, consumer);

    if (stores.containsKey(key)) {
      consumer.accept(stores.get(key));
    }
    return () -> consumers.get(key).remove(consumer);
  }

  public <T> RegistrationHandler consumeData(String storeKey, Consumer<T> consumer) {
    RegistrationHandler[] registrationHandler = new RegistrationHandler[1];
    Consumer<IsStore<?>> isStoreConsumer =
        isStore -> registrationHandler[0] = ((IsStore<T>) isStore).consumeData(consumer);
    addConsumer(storeKey, isStoreConsumer);

    if (stores.containsKey(storeKey)) {
      isStoreConsumer.accept(stores.get(storeKey));
    }
    return registrationHandler[0];
  }

  public boolean containsStore(String storeKey) {
    return stores.containsKey(storeKey);
  }
}
