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
package org.dominokit.domino.api.client.extension;

import java.util.Set;
import org.dominokit.domino.api.shared.extension.DominoEvent;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.extension.GlobalDominoEventListener;

public interface DominoEventsListenersRepository {
  void addListener(
      Class<? extends DominoEvent> dominoEvent, DominoEventListener dominoEventListener);

  void addGlobalListener(
      Class<? extends DominoEvent> dominoEvent, GlobalDominoEventListener dominoEventListener);

  Set<DominoEventListener> getEventListeners(Class<? extends DominoEvent> dominoEvent);

  void removeListener(Class<? extends DominoEvent> event, DominoEventListener listener);

  void removeGlobalListener(Class<? extends DominoEvent> event, GlobalDominoEventListener listener);

  void fireEvent(Class<? extends DominoEvent> eventType, DominoEvent dominoEvent);
}
