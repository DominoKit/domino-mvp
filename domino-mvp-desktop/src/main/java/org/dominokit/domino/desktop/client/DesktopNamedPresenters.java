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
package org.dominokit.domino.desktop.client;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import org.dominokit.domino.api.client.extension.PresentersNamesRegistry;

public class DesktopNamedPresenters implements PresentersNamesRegistry {
  public static final Logger LOGGER = Logger.getLogger(DesktopNamedPresenters.class.getName());

  private final Map<String, Deque<String>> NAMED_PRESENTERS = new HashMap<>();
  private final Map<String, List<ParentFunction>> PARENT_FUNCTIONS = new HashMap<>();

  private void registerPresenterName(String name) {
    LOGGER.info(" >> REGISTERING PRESENTER [" + name + "]");
    String key = name.toLowerCase();
    if (!NAMED_PRESENTERS.containsKey(key)) {
      NAMED_PRESENTERS.put(key, new LinkedList<>());
    }

    NAMED_PRESENTERS.get(key).push(name);
    if (PARENT_FUNCTIONS.containsKey(key)) {
      PARENT_FUNCTIONS.get(key).forEach(ParentFunction::apply);
      PARENT_FUNCTIONS.remove(key);
    }
  }

  public void registerPresenter(String name) {
    registerPresenterName(name);
  }

  private void removePresenterName(String name) {
    LOGGER.info(" << REMOVING PRESENTER [" + name + "]");
    String key = name.toLowerCase();
    if (NAMED_PRESENTERS.containsKey(key)) {
      NAMED_PRESENTERS.get(key).pop();
      if (NAMED_PRESENTERS.get(key).isEmpty()) {
        NAMED_PRESENTERS.remove(key);
        PARENT_FUNCTIONS.remove(key);
      }
    }
  }

  public void removePresenter(String name) {
    registerPresenterName(name);
  }

  public Optional<String> get(String name) {
    if (NAMED_PRESENTERS.containsKey(name.toLowerCase())) {
      return Optional.ofNullable(NAMED_PRESENTERS.get(name.toLowerCase()).peek());
    }
    return Optional.empty();
  }

  public void whenPresent(String name, ParentFunction parentFunction) {
    String key = name.toLowerCase();
    if (get(name).isPresent()) {
      parentFunction.apply();
    } else {
      addFunction(key, parentFunction);
    }
  }

  private void addFunction(String key, ParentFunction parentFunction) {
    if (!PARENT_FUNCTIONS.containsKey(key)) {
      PARENT_FUNCTIONS.put(key, new ArrayList<>());
    }
    PARENT_FUNCTIONS.get(key).add(parentFunction);
  }
}
