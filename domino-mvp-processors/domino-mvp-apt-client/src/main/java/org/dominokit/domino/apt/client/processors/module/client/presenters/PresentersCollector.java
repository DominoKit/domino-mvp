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
package org.dominokit.domino.apt.client.processors.module.client.presenters;

import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import org.dominokit.domino.api.client.annotations.presenter.Presenter;
import org.dominokit.domino.api.client.mvp.presenter.Presentable;
import org.dominokit.domino.apt.commons.BaseProcessor;
import org.dominokit.domino.apt.commons.ProcessorElement;

public class PresentersCollector {

  private final Messager messager;
  private final Types typeUtils;
  private final Elements elementUtils;
  private final BaseProcessor.ElementFactory elementFactory;
  private final Set<String> presenters;

  public PresentersCollector(
      Messager messager,
      Types typeUtils,
      Elements elementUtils,
      BaseProcessor.ElementFactory elementFactory,
      Set<String> presenters) {
    this.messager = messager;
    this.typeUtils = typeUtils;
    this.elementUtils = elementUtils;
    this.elementFactory = elementFactory;
    this.presenters = presenters;
  }

  public void collectPresenters(RoundEnvironment roundEnv) {
    roundEnv.getElementsAnnotatedWith(Presenter.class).stream()
        .map(elementFactory::make)
        .filter(e -> e.validateElementKind(ElementKind.CLASS))
        .collect(Collectors.toSet())
        .forEach(this::addPresenter);
  }

  private boolean addPresenter(ProcessorElement p) {
    if (!p.isAssignableFrom(Presentable.class)) {
      messager.printMessage(
          Diagnostic.Kind.ERROR, "Not implementing presentable interface", p.getElement());
      throw new NotImplementingPresentableInterfaceException();
    }
    return presenters.add(p.fullQualifiedNoneGenericName());
  }

  public class NotImplementingPresentableInterfaceException extends RuntimeException {}
}
