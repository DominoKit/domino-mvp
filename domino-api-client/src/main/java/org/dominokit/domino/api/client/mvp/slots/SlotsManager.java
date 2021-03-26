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
package org.dominokit.domino.api.client.mvp.slots;

import org.dominokit.domino.api.client.mvp.view.HasContent;

public interface SlotsManager {
  void registerSlot(String key, IsSlot slot);

  void removeSlot(String key);

  void revealView(String slotKey, HasContent view, HasContent.CreateHandler createHandler);
}
