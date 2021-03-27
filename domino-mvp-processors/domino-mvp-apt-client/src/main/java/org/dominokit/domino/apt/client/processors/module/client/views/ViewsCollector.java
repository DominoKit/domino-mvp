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
package org.dominokit.domino.apt.client.processors.module.client.views;

import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.tools.Diagnostic;
import org.dominokit.domino.api.client.annotations.UiView;
import org.dominokit.domino.api.client.mvp.view.View;
import org.dominokit.domino.apt.commons.BaseProcessor;
import org.dominokit.domino.apt.commons.ProcessorElement;

public class ViewsCollector {

  private final BaseProcessor.ElementFactory elementFactory;
  private final Set<String> views;
  private final Messager messager;

  public ViewsCollector(
      Messager messager, BaseProcessor.ElementFactory elementFactory, Set<String> views) {
    this.messager = messager;
    this.elementFactory = elementFactory;
    this.views = views;
  }

  public void collectViews(RoundEnvironment roundEnv) {
    roundEnv.getElementsAnnotatedWith(UiView.class).stream()
        .map(elementFactory::make)
        .filter(e -> e.validateElementKind(ElementKind.CLASS))
        .collect(Collectors.toSet())
        .forEach(this::addView);
  }

  private boolean addView(ProcessorElement v) {
    isView(v);
    return views.add(v.asTypeElement().getQualifiedName().toString());
  }

  private void isView(ProcessorElement element) {
    if (element.isImplementsGenericInterface(View.class))
      messager.printMessage(
          Diagnostic.Kind.WARNING,
          "Class is annotated as View while it is not implementing view interface.!");
  }
}
