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

import com.google.auto.common.MoreElements;
import com.squareup.javapoet.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import org.dominokit.domino.api.client.annotations.presenter.ListenTo;
import org.dominokit.domino.api.client.annotations.presenter.Listener;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.extension.GlobalDominoEventListener;
import org.dominokit.domino.api.shared.extension.GlobalEvent;
import org.dominokit.domino.apt.commons.AbstractSourceBuilder;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;

public class DominoEventListenerSourceWriter extends AbstractSourceBuilder {

  private final Element presenterElement;
  private final Element root;
  private final TypeElement eventType;

  public DominoEventListenerSourceWriter(
      Element presenterElement, Element listenMethod, ProcessingEnvironment processingEnvironment) {
    super(processingEnvironment);
    this.presenterElement = presenterElement;
    this.root = listenMethod;
    this.eventType = getEventType(root);
  }

  private String makeRequestClassName() {
    return elements.getPackageOf(presenterElement).getQualifiedName().toString()
        + "."
        + presenterElement.getSimpleName().toString()
        + "Command";
  }

  @Override
  public List<TypeSpec.Builder> asTypeBuilder() {

    String eventClassName =
        presenterElement.getSimpleName() + "ListenFor" + eventType.getSimpleName();
    boolean isGlobalEvent = processorUtil.isAssignableFrom(eventType, GlobalEvent.class);
    ClassName listenerSuperType =
        isGlobalEvent
            ? ClassName.get(GlobalDominoEventListener.class)
            : ClassName.get(DominoEventListener.class);

    TypeSpec.Builder listenerType =
        DominoTypeBuilder.classBuilder(eventClassName, PresenterProcessor.class)
            .addAnnotation(Listener.class)
            .addSuperinterface(
                ParameterizedTypeName.get(listenerSuperType, TypeName.get(eventType.asType())))
            .addField(
                FieldSpec.builder(
                        TypeName.get(presenterElement.asType()),
                        "presenter",
                        Modifier.PRIVATE,
                        Modifier.FINAL)
                    .build())
            .addMethod(
                MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(TypeName.get(presenterElement.asType()), "presenter")
                    .addStatement("this.presenter = presenter")
                    .build())
            .addMethod(makeListenMethod());

    if (isGlobalEvent) {
      listenerType.addMethod(makeCreateEventMethod(eventType));
    }
    return Collections.singletonList(listenerType);
  }

  private TypeElement getEventType(Element e) {
    AnnotationMirror providerAnnotation = MoreElements.getAnnotationMirror(e, ListenTo.class).get();
    DeclaredType providerInterface = this.getProviderInterface(providerAnnotation);
    TypeElement typeElement = asTypeElement(providerInterface);
    return typeElement;
  }

  private DeclaredType getProviderInterface(AnnotationMirror providerAnnotation) {
    Map valueIndex = providerAnnotation.getElementValues();
    AnnotationValue value = (AnnotationValue) valueIndex.values().iterator().next();
    return (DeclaredType) value.getValue();
  }

  private TypeElement asTypeElement(DeclaredType p) {
    return (TypeElement) p.asElement();
  }

  private MethodSpec makeListenMethod() {
    return MethodSpec.methodBuilder("onEventReceived")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .addParameter(TypeName.get(eventType.asType()), "event")
        .addStatement("this.presenter.$L(event.context())", root.getSimpleName().toString())
        .build();
  }

  private MethodSpec makeCreateEventMethod(TypeElement eventType) {
    return MethodSpec.methodBuilder("deserializeEvent")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .returns(TypeName.get(eventType.asType()))
        .addParameter(TypeName.get(String.class), "serializedEvent")
        .addStatement("return new $T(serializedEvent)", TypeName.get(eventType.asType()))
        .build();
  }
}
