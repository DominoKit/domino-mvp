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
package org.dominokit.domino.apt.client.processors.module.client.presenters;

import com.google.auto.service.AutoService;
import org.dominokit.domino.api.client.annotations.Aggregate;
import org.dominokit.domino.api.client.annotations.PresenterProxy;
import org.dominokit.domino.apt.client.processors.aggregate.AggregateProcessingStep;
import org.dominokit.domino.apt.commons.BaseProcessor;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
public class PresenterProxyProcessor extends BaseProcessor {

  private final Set<String> supportedAnnotations = new HashSet<>();

  public PresenterProxyProcessor() {
    supportedAnnotations.add(PresenterProxy.class.getCanonicalName());
    supportedAnnotations.add(Aggregate.class.getCanonicalName());
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return supportedAnnotations;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    new AggregateProcessingStep.Builder()
            .setProcessingEnv(processingEnv)
            .build()
            .process(roundEnv.getElementsAnnotatedWith(Aggregate.class));

    new PresenterProxyProcessingStep.Builder()
            .setProcessingEnv(processingEnv)
            .build()
            .process(roundEnv.getElementsAnnotatedWith(PresenterProxy.class));
    return false;
  }

}
