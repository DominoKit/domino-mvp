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
package org.dominokit.domino.gwt.client.extensions;

import static java.util.Objects.nonNull;

import elemental2.core.JsObject;
import elemental2.dom.CustomEvent;
import elemental2.dom.CustomEventInit;
import elemental2.dom.DomGlobal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.extension.PresentersNamesRegistry;

public class WebNamedPresenters implements PresentersNamesRegistry {
  public static final Logger LOGGER = Logger.getLogger(WebNamedPresenters.class.getName());

  private final Map<String, Deque<String>> NAMED_PRESENTERS = new HashMap<>();
  private final Map<String, List<ParentFunction>> PARENT_FUNCTIONS = new HashMap<>();

  public WebNamedPresenters() {
    DomGlobal.document.addEventListener(
        "register-domino-presenter-name",
        evt -> {
          CustomEvent<String> customEvent = Js.uncheckedCast(evt);
          String presenterName = customEvent.detail;
          if (!get(presenterName).isPresent()) {
            LOGGER.info("Global register presenter name event received : [" + presenterName + "].");
            registerPresenterName(presenterName);
          }
        });

    DomGlobal.document.addEventListener(
        "remove-domino-presenter-name",
        evt -> {
          CustomEvent<String> customEvent = Js.uncheckedCast(evt);
          String presenterName = customEvent.detail;
          if (get(presenterName).isPresent()) {
            LOGGER.info("Global remove presenter name event received : [" + presenterName + "].");
            removePresenterName(customEvent.detail);
          }
        });
  }

  @Override
  public void init() {
    if (ClientApp.make().dominoOptions().isMainApp()) {
      return;
    }
    JsPropertyMap<JsObject> windowAsMap = Js.uncheckedCast(DomGlobal.window);
    String namedPresenters = Js.uncheckedCast(windowAsMap.get("domino-mvp-presenters"));
    if (nonNull(namedPresenters)) {
      LOGGER.info("Reading already registered presenters : " + namedPresenters);
      Arrays.asList(namedPresenters.split(",")).forEach(this::registerPresenterName);
    }
  }

  private void registerPresenterName(String name) {
    LOGGER.info(" >> REGISTERING PRESENTER [" + name + "]");
    String key = name.toLowerCase();
    if (!NAMED_PRESENTERS.containsKey(key)) {
      NAMED_PRESENTERS.put(key, new LinkedList<>());
    }

    NAMED_PRESENTERS.get(key).push(name);
    if (PARENT_FUNCTIONS.containsKey(key)) {
      PARENT_FUNCTIONS.get(key).forEach(ParentFunction::apply);
      PARENT_FUNCTIONS.get(key).clear();
    }

    if (ClientApp.make().dominoOptions().isMainApp()) {
      JsPropertyMap<JsObject> windowAsMap = Js.uncheckedCast(DomGlobal.window);
      windowAsMap.set(
          "domino-mvp-presenters", Js.uncheckedCast(String.join(",", NAMED_PRESENTERS.keySet())));
    }
  }

  public void registerPresenter(String name) {
    registerPresenterName(name);
    DomGlobal.document.dispatchEvent(createCustomEvent("register-domino-presenter-name", name));
    LOGGER.info("Registering Presenter name event fired : " + name);
  }

  private CustomEvent<String> createCustomEvent(String type, String presenterName) {
    CustomEventInit<String> customEventInit = CustomEventInit.create();
    customEventInit.setDetail(presenterName);
    return new CustomEvent<>(type, customEventInit);
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
    if (ClientApp.make().dominoOptions().isMainApp()) {
      JsPropertyMap<JsObject> windowAsMap = Js.uncheckedCast(DomGlobal.window);
      windowAsMap.set(
          "domino-mvp-presenters", Js.uncheckedCast(String.join(",", NAMED_PRESENTERS.keySet())));
    }
  }

  public void removePresenter(String name) {
    registerPresenterName(name);
    DomGlobal.document.dispatchEvent(createCustomEvent("remove-domino-presenter-name", name));
    LOGGER.info("Removing Presenter name event fired : " + name);
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
      LOGGER.info(">> Parent presenter : " + name + " already registered, applying function");
      parentFunction.apply();
    } else {
      LOGGER.info(
          "<< Parent presenter : "
              + name
              + " not registered yet, adding function to waiting list: ");
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
