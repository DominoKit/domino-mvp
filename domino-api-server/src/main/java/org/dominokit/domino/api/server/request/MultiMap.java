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
package org.dominokit.domino.api.server.request;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MultiMap<K, V> extends Iterable<Map.Entry<K, V>> {
  V get(K name);

  List<V> getAll(K name);

  List<Map.Entry<K, V>> entries();

  boolean contains(K name);

  boolean isEmpty();

  Set<K> names();

  MultiMap<K, V> add(K name, V value);

  MultiMap<K, V> add(K name, Iterable<V> values);

  MultiMap<K, V> addAll(MultiMap<K, V> map);

  MultiMap<K, V> addAll(Map<K, V> values);

  MultiMap<K, V> set(K name, V value);

  MultiMap<K, V> set(K name, Iterable<V> values);

  MultiMap<K, V> setAll(MultiMap<K, V> map);

  MultiMap<K, V> setAll(Map<K, V> values);

  MultiMap<K, V> remove(K name);

  MultiMap<K, V> clear();

  int size();
}
