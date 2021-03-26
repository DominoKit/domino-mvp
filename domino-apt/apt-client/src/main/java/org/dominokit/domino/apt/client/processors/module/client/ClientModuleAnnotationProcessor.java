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
package org.dominokit.domino.apt.client.processors.module.client;

import com.google.auto.service.AutoService;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.dominokit.domino.api.client.annotations.ClientModule;
import org.dominokit.domino.api.client.annotations.StartupTask;
import org.dominokit.domino.api.client.annotations.UiView;
import org.dominokit.domino.api.client.annotations.presenter.Presenter;
import org.dominokit.domino.apt.client.processors.module.client.initialtasks.InitialTasksCollector;
import org.dominokit.domino.apt.client.processors.module.client.presenters.PresentersCollector;
import org.dominokit.domino.apt.client.processors.module.client.views.ViewsCollector;
import org.dominokit.domino.apt.commons.BaseProcessor;

@AutoService(Processor.class)
public class ClientModuleAnnotationProcessor extends BaseProcessor {

  private static final Logger LOGGER =
      Logger.getLogger(ClientModuleAnnotationProcessor.class.getName());

  private Set<String> presenters;
  private Set<String> views;
  private Set<String> initialTasks;
  private Set<Element> clientModules = new HashSet<>();

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

    try {
      clientModules.addAll(roundEnv.getElementsAnnotatedWith(ClientModule.class));

      roundEnv.getElementsAnnotatedWith(Presenter.class);
      roundEnv.getElementsAnnotatedWith(UiView.class);
      roundEnv.getElementsAnnotatedWith(StartupTask.class);

      Register presentersRegister = new Register("presenters", presenters, messager, processingEnv);
      Register viewsRegister = new Register("views", views, messager, processingEnv);
      Register initialTasksRegister =
          new Register("initialTasks", initialTasks, messager, processingEnv);

      presenters = presentersRegister.readItems();
      views = viewsRegister.readItems();
      initialTasks = initialTasksRegister.readItems();

      if (roundEnv.processingOver()) {
        presentersRegister.writeItems();
        viewsRegister.writeItems();
        initialTasksRegister.writeItems();
        if (roundEnv.processingOver()) generateModuleConfiguration();
        return true;
      }

      new PresentersCollector(messager, typeUtils, elementUtils, elementFactory, presenters)
          .collectPresenters(roundEnv);
      new ViewsCollector(messager, elementFactory, views).collectViews(roundEnv);
      new InitialTasksCollector(elementFactory, initialTasks).collectInitialTasks(roundEnv);
    } catch (Exception e) {
      messager.printMessage(Diagnostic.Kind.ERROR, ExceptionUtils.getFullStackTrace(e));
    }
    return false;
  }

  private void generateModuleConfiguration() {

    new ClientModuleProcessingStep.Builder()
        .setPresenters(presenters)
        .setInitialTasks(initialTasks)
        .setViews(views)
        .setProcessingEnv(processingEnv)
        .build()
        .process(clientModules);
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> annotations = new HashSet<>();
    annotations.add(Presenter.class.getCanonicalName());
    annotations.add(UiView.class.getCanonicalName());
    annotations.add(StartupTask.class.getCanonicalName());
    annotations.add(ClientModule.class.getCanonicalName());
    return annotations;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }
}
