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
package org.dominokit.domino.apt.client.processors.module.client.presenters.store;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.annotations.store.Store;
import org.dominokit.domino.api.client.mvp.presenter.AbstractStore;
import org.dominokit.domino.api.client.mvp.presenter.DominoStore;
import org.dominokit.domino.api.shared.annotations.events.EventContext;
import org.dominokit.domino.apt.client.processors.module.client.events.DominoEventsProcessor;
import org.dominokit.domino.apt.commons.AbstractSourceBuilder;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;

public class DominoStoreSourceWriter extends AbstractSourceBuilder {

  private final Element storeElement;

  protected DominoStoreSourceWriter(Element storeElement, ProcessingEnvironment processingEnv) {
    super(processingEnv);
    this.storeElement = storeElement;
  }

  @Override
  public List<TypeSpec.Builder> asTypeBuilder() {

    List<TypeSpec.Builder> types = new ArrayList<>();

    if (storeElement.getKind().isInterface()) {
      ((TypeElement) storeElement)
          .getInterfaces().stream()
              .filter(typeMirror -> processorUtil.isAssignableFrom(typeMirror, DominoStore.class))
              .findFirst()
              .ifPresent(storeMarkerInterface -> types.add(generateStore(storeMarkerInterface)));
    }

    return types;
  }

  private TypeSpec.Builder generateStore(TypeMirror storeMarkerInterface) {
    Store storeAnnotation = storeElement.getAnnotation(Store.class);
    TypeMirror storeDataType = processorUtil.firstTypeArgument(storeMarkerInterface);
    String storeName =
        processorUtil.capitalizeFirstLetter(
            isNullOrEmpty(storeAnnotation.value())
                ? storeElement.getSimpleName() + "Impl"
                : storeAnnotation.value());

    TypeSpec.Builder storeType =
        DominoTypeBuilder.classBuilder(storeName, DominoEventsProcessor.class)
            .superclass(
                ParameterizedTypeName.get(
                    ClassName.get(AbstractStore.class), TypeName.get(storeDataType)))
            .addModifiers(Modifier.PUBLIC);

    if (storeAnnotation.isAbstract()) {
      storeType.addModifiers(Modifier.ABSTRACT);
    } else {
      storeType.addField(
          FieldSpec.builder(
                  ClassName.bestGuess(storeName),
                  "INSTANCE",
                  Modifier.PUBLIC,
                  Modifier.STATIC,
                  Modifier.FINAL)
              .initializer("new $T()", ClassName.bestGuess(storeName))
              .build());
    }

    storeType
        .addSuperinterface(storeElement.asType())
        .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build());

    EventContext eventContext = types.asElement(storeDataType).getAnnotation(EventContext.class);
    if (nonNull(eventContext)) {

      String eventName =
          processorUtil.capitalizeFirstLetter(
              isNullOrEmpty(eventContext.value())
                  ? types.asElement(storeDataType).getSimpleName() + "Event"
                  : eventContext.value());
      String eventPackage =
          elements.getPackageOf(types.asElement(storeDataType)).getQualifiedName().toString();
      storeType.addMethod(
          MethodSpec.methodBuilder("fireEvent")
              .addAnnotation(Override.class)
              .addModifiers(Modifier.PROTECTED)
              .returns(TypeName.get(void.class))
              .addStatement(
                  "$T.make().fireEvent($T.class, new $T(getData().get()))",
                  ClientApp.class,
                  ClassName.bestGuess(eventPackage + "." + eventName),
                  ClassName.bestGuess(eventPackage + "." + eventName))
              .build());
    }

    return storeType;
  }

  private boolean isNullOrEmpty(String value) {
    return isNull(value) || value.trim().isEmpty();
  }
}
