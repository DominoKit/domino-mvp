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

import static java.util.Objects.nonNull;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import org.dominokit.domino.api.client.InitialTaskRegistry;
import org.dominokit.domino.api.client.ModuleConfiguration;
import org.dominokit.domino.api.client.annotations.ClientModule;
import org.dominokit.domino.api.client.annotations.UiView;
import org.dominokit.domino.api.client.annotations.presenter.PresenterProxy;
import org.dominokit.domino.api.client.annotations.presenter.Singleton;
import org.dominokit.domino.api.client.mvp.presenter.PresenterSupplier;
import org.dominokit.domino.api.client.mvp.presenter.ViewBaseClientPresenter;
import org.dominokit.domino.api.client.mvp.presenter.ViewablePresenterSupplier;
import org.dominokit.domino.api.client.mvp.view.View;
import org.dominokit.domino.apt.commons.AbstractSourceBuilder;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;
import org.dominokit.domino.apt.commons.ExceptionUtil;

public class ModuleConfigurationSourceWriter extends AbstractSourceBuilder {

  private final Set<String> initialTasks;
  private final Set<String> presenters;
  private final Set<String> views;
  private final Element moduleElement;

  public ModuleConfigurationSourceWriter(
      Element moduleElement,
      Set<String> presenters,
      Set<String> views,
      Set<String> initialTasks,
      ProcessingEnvironment processingEnvironment) {
    super(processingEnvironment);
    this.moduleElement = moduleElement;
    this.presenters = presenters;
    this.views = views;
    this.initialTasks = initialTasks;
  }

  @Override
  public List<TypeSpec.Builder> asTypeBuilder() {
    TypeSpec.Builder clientModuleTypeBuilder =
        DominoTypeBuilder.classBuilder(
                moduleElement.getAnnotation(ClientModule.class).name() + "ModuleConfiguration",
                ClientModuleAnnotationProcessor.class)
            .addSuperinterface(ModuleConfiguration.class);

    if (!presenters.isEmpty()) {
      clientModuleTypeBuilder.addMethod(registerPresenters());
    }

    if (!views.isEmpty()) {
      clientModuleTypeBuilder.addMethod(registerViews());
    }

    if (!initialTasks.isEmpty()) {
      clientModuleTypeBuilder.addMethod(registerInitialTasks());
    }

    return Collections.singletonList(clientModuleTypeBuilder);
  }

  private MethodSpec registerPresenters() {

    MethodSpec.Builder methodBuilder =
        MethodSpec.methodBuilder("registerPresenters")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC);

    presenters.stream()
        .map(elements::getTypeElement)
        .forEach(
            presenter -> {
              ClassName configClassName =
                  ClassName.bestGuess(
                      elements.getPackageOf(presenter).getQualifiedName().toString()
                          + "."
                          + presenter.getSimpleName().toString()
                          + "_Config");
              String configName =
                  processorUtil.lowerFirstLetter(presenter.getSimpleName().toString() + "_Config");
              boolean singleton =
                  nonNull(presenter.getAnnotation(Singleton.class))
                      && presenter.getAnnotation(Singleton.class).value();

              methodBuilder.addStatement(
                  "$T $L = new $T()", configClassName, configName, configClassName);
              if (processorUtil.isAssignableFrom(presenter, ViewBaseClientPresenter.class)) {
                processorUtil
                    .findTypeArgument(presenter.asType(), View.class)
                    .ifPresent(
                        viewType ->
                            methodBuilder.addStatement(
                                "$L.setPresenterSupplier(new $T<$T, $T>($L, ()-> new $T()))",
                                configName,
                                TypeName.get(ViewablePresenterSupplier.class),
                                TypeName.get(presenter.asType()),
                                TypeName.get(viewType),
                                singleton,
                                TypeName.get(presenter.asType())));

              } else {
                methodBuilder.addStatement(
                    "$L.setPresenterSupplier(new $T<$T>($L, ()-> new $T()))",
                    configName,
                    TypeName.get(PresenterSupplier.class),
                    TypeName.get(presenter.asType()),
                    singleton,
                    TypeName.get(presenter.asType()));
              }

              methodBuilder.addCode("\n");
            });

    return methodBuilder.build();
  }

  private MethodSpec registerViews() {

    MethodSpec.Builder methodBuilder =
        MethodSpec.methodBuilder("registerViews")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC);

    views.stream()
        .map(
            charSequence -> {
              String viewElement = charSequence;
              return elements.getTypeElement(viewElement);
            })
        .forEach(
            view -> {
              try {
                List<TypeMirror> presentersTypes =
                    processorUtil.getClassArrayValueFromAnnotation(
                        view, UiView.class, "presentable");

                if (presentersTypes.isEmpty()) {
                  throw new IllegalArgumentException();
                }
                presentersTypes.forEach(
                    presenter -> {
                      boolean proxy =
                          nonNull(types.asElement(presenter).getAnnotation(PresenterProxy.class));

                      String postfix = (proxy ? "_Presenter" : "") + "_Config";
                      ClassName configClassName =
                          ClassName.bestGuess(
                              elements
                                      .getPackageOf(types.asElement(presenter))
                                      .getQualifiedName()
                                      .toString()
                                  + "."
                                  + types.asElement(presenter).getSimpleName().toString()
                                  + postfix);
                      String configName =
                          processorUtil.lowerFirstLetter(
                              types.asElement(presenter).getSimpleName().toString() + postfix);

                      methodBuilder.addStatement(
                          "$T $L = new $T()", configClassName, configName, configClassName);
                      methodBuilder.addStatement(
                          "$L.setViewSupplier(()-> new $T())",
                          configName,
                          TypeName.get(view.asType()));
                    });

              } catch (Exception e) {
                ExceptionUtil.messageStackTrace(messager, e, view);
              }
            });

    return methodBuilder.build();
  }

  private MethodSpec registerInitialTasks() {

    MethodSpec.Builder methodBuilder =
        MethodSpec.methodBuilder("registerInitialTasks")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(TypeName.get(InitialTaskRegistry.class), "registry");
    initialTasks.stream()
        .map(elements::getTypeElement)
        .forEach(
            initialTask -> {
              methodBuilder.addStatement(
                  "registry.registerInitialTask(new $T())", TypeName.get(initialTask.asType()));
            });

    return methodBuilder.build();
  }
}
