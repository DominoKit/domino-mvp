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
package org.dominokit.domino.apt.client.processors.module.client.events;

import static java.util.Objects.isNull;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import org.dominokit.domino.api.shared.annotations.events.EventContext;
import org.dominokit.domino.api.shared.extension.DominoEvent;
import org.dominokit.domino.apt.client.processors.module.client.presenters.PresenterProcessor;
import org.dominokit.domino.apt.commons.AbstractSourceBuilder;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;

public class DominoEventSourceWriter extends AbstractSourceBuilder {

  private final Element eventElement;

  protected DominoEventSourceWriter(Element eventElement, ProcessingEnvironment processingEnv) {
    super(processingEnv);
    this.eventElement = eventElement;
  }

  @Override
  public List<TypeSpec.Builder> asTypeBuilder() {
    EventContext eventContext = eventElement.getAnnotation(EventContext.class);
    String eventName =
        processorUtil.capitalizeFirstLetter(
            isNullOrEmpty(eventContext.value())
                ? eventElement.getSimpleName() + "Event"
                : eventContext.value());

    String fieldName = processorUtil.smallFirstLetter(eventElement.getSimpleName().toString());
    TypeSpec.Builder eventType =
        DominoTypeBuilder.classBuilder(eventName, PresenterProcessor.class)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(DominoEvent.class)
            .addField(
                FieldSpec.builder(
                        TypeName.get(eventElement.asType()),
                        fieldName,
                        Modifier.PRIVATE,
                        Modifier.FINAL)
                    .build())
            .addMethod(
                MethodSpec.constructorBuilder()
                    .addParameter(TypeName.get(eventElement.asType()), fieldName)
                    .addStatement("this.$L = $L", fieldName, fieldName)
                    .build())
            .addMethod(
                MethodSpec.methodBuilder("get" + processorUtil.capitalizeFirstLetter(fieldName))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.get(eventElement.asType()))
                    .addParameter(TypeName.get(eventElement.asType()), fieldName)
                    .addStatement("return this.$L", fieldName)
                    .build());

    List<TypeSpec.Builder> types = new ArrayList<>();
    types.add(eventType);
    return types;
  }

  private boolean isNullOrEmpty(String value) {
    return isNull(value) || value.isEmpty();
  }
}
