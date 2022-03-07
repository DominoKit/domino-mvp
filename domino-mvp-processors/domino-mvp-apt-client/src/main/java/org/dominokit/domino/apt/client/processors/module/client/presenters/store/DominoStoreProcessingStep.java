/*
 * Copyright © 2018 The GWT Authors
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
package org.dominokit.domino.apt.client.processors.module.client.presenters.store;

import static java.util.Objects.nonNull;

import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import org.dominokit.domino.api.client.annotations.store.Store;
import org.dominokit.domino.apt.commons.AbstractProcessingStep;
import org.dominokit.domino.apt.commons.ExceptionUtil;
import org.dominokit.domino.apt.commons.StepBuilder;

public class DominoStoreProcessingStep extends AbstractProcessingStep {

  public DominoStoreProcessingStep(ProcessingEnvironment processingEnv) {
    super(processingEnv);
  }

  public static class Builder extends StepBuilder<DominoStoreProcessingStep> {

    public DominoStoreProcessingStep build() {
      return new DominoStoreProcessingStep(processingEnv);
    }
  }

  @Override
  public void process(Set<? extends Element> elementsByAnnotation) {

    for (Element element : elementsByAnnotation) {
      try {
        generateEvent(element);
      } catch (Exception e) {
        ExceptionUtil.messageStackTrace(messager, e, element);
      }
    }
  }

  private void generateEvent(Element eventElement) {

    Store event = eventElement.getAnnotation(Store.class);
    if (nonNull(event)) {
      writeSource(
          new DominoStoreSourceWriter(eventElement, processingEnv).asTypeBuilder(),
          elements.getPackageOf(eventElement).getQualifiedName().toString());
    }
  }
}
