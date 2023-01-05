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
import static java.util.Objects.nonNull;

import dominojackson.shaded.com.squareup.javapoet.ClassName;
import dominojackson.shaded.com.squareup.javapoet.CodeBlock;
import dominojackson.shaded.com.squareup.javapoet.FieldSpec;
import dominojackson.shaded.com.squareup.javapoet.MethodSpec;
import dominojackson.shaded.com.squareup.javapoet.ParameterSpec;
import dominojackson.shaded.com.squareup.javapoet.ParameterizedTypeName;
import dominojackson.shaded.com.squareup.javapoet.TypeName;
import dominojackson.shaded.com.squareup.javapoet.TypeSpec;
import dominojackson.shaded.com.squareup.javapoet.TypeVariableName;
import dominojackson.shaded.org.dominokit.domino.apt.commons.AbstractSourceBuilder;
import dominojackson.shaded.org.dominokit.domino.apt.commons.DominoTypeBuilder;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import org.dominokit.domino.api.shared.annotations.events.EventContext;
import org.dominokit.domino.api.shared.extension.DominoEvent;
import org.dominokit.domino.api.shared.extension.GlobalEvent;
import org.dominokit.domino.api.shared.extension.IsDominoEvent;
import org.dominokit.jackson.AbstractObjectReader;
import org.dominokit.jackson.AbstractObjectWriter;
import org.dominokit.jackson.JsonDeserializer;
import org.dominokit.jackson.JsonSerializer;
import org.dominokit.jackson.annotation.JSONMapper;
import org.dominokit.jackson.annotation.JSONReader;
import org.dominokit.jackson.annotation.JSONWriter;
import org.dominokit.jackson.processor.deserialization.FieldDeserializersChainBuilder;
import org.dominokit.jackson.processor.serialization.FieldSerializerChainBuilder;

public class DominoEventSourceWriter extends AbstractSourceBuilder {

  private final Element eventElement;

  protected DominoEventSourceWriter(Element eventElement, ProcessingEnvironment processingEnv) {
    super(processingEnv);
    this.eventElement = eventElement;
  }

  @Override
  public List<TypeSpec.Builder> asTypeBuilder() {

    List<TypeSpec.Builder> types = new ArrayList<>();
    EventContext eventContext = eventElement.getAnnotation(EventContext.class);

    boolean markerInterface = processorUtil.isAssignableFrom(eventElement, IsDominoEvent.class);
    TypeSpec.Builder eventType;
    if (markerInterface) {
      generateMarkerInterfaceEvent(eventContext).ifPresent(types::add);
    } else {
      types.add(generatePojoEvent(eventContext, eventElement));
    }

    return types;
  }

  private Optional<TypeSpec.Builder> generateMarkerInterfaceEvent(EventContext eventContext) {
    boolean implementsIsDominoEvent =
        processorUtil.isAssignableFrom(eventElement, IsDominoEvent.class);
    if (!implementsIsDominoEvent) {
      processingEnv
          .getMessager()
          .printMessage(
              Diagnostic.Kind.ERROR,
              "Event interface does not extends from IsDominoEvent",
              eventElement);
    }

    Optional<? extends TypeMirror> first =
        ((TypeElement) eventElement)
            .getInterfaces().stream()
                .filter(
                    typeMirror -> processorUtil.isAssignableFrom(typeMirror, IsDominoEvent.class))
                .findFirst();

    final TypeSpec.Builder builder;

    if (first.isPresent()) {
      TypeMirror argumentType = processorUtil.firstTypeArgument(first.get());
      Element targetElement = types.asElement(argumentType);
      return Optional.of(generatePojoEvent(eventContext, targetElement));
    } else {
      return Optional.empty();
    }
  }

  private TypeSpec.Builder generatePojoEvent(EventContext eventContext, Element targetElement) {
    String eventName =
        processorUtil.capitalizeFirstLetter(
            isNullOrEmpty(eventContext.value())
                ? targetElement.getSimpleName() + "Event"
                : eventContext.value());

    String fieldName = processorUtil.smallFirstLetter(targetElement.getSimpleName().toString());
    TypeSpec.Builder eventType =
        DominoTypeBuilder.classBuilder(eventName, DominoEventsProcessor.class)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(eventContext.global() ? GlobalEvent.class : DominoEvent.class)
            .addField(
                FieldSpec.builder(
                        TypeName.get(targetElement.asType()),
                        fieldName,
                        Modifier.PRIVATE,
                        Modifier.FINAL)
                    .build())
            .addMethod(
                MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(TypeName.get(targetElement.asType()), fieldName)
                    .addStatement("this.$L = $L", fieldName, fieldName)
                    .build());

    if (eventContext.global()) {
      eventType
          .addMethod(
              MethodSpec.methodBuilder("deserialize")
                  .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                  .returns(TypeName.get(targetElement.asType()))
                  .addParameter(ParameterSpec.builder(TypeName.get(String.class), "data").build())
                  .addStatement(
                      "return $L.read(data)", getEventDeserializer(targetElement.asType()))
                  .build())
          .addMethod(
              MethodSpec.methodBuilder("serialize")
                  .addAnnotation(Override.class)
                  .addModifiers(Modifier.PUBLIC)
                  .returns(TypeName.get(String.class))
                  .addStatement(
                      "return $L.write(this.$L)",
                      getEventSerializer(targetElement.asType()),
                      fieldName)
                  .build());
    }

    eventType.addMethod(
        MethodSpec.methodBuilder("get" + processorUtil.capitalizeFirstLetter(fieldName))
            .addModifiers(Modifier.PUBLIC)
            .returns(TypeName.get(targetElement.asType()))
            .addStatement("return this.$L", fieldName)
            .build());
    return eventType;
  }

  public CodeBlock getEventSerializer(TypeMirror eventDataType) {
    boolean serializerGenerated = !shouldGenerateSerializer(eventDataType);
    CodeBlock.Builder builder = CodeBlock.builder();
    CodeBlock instance =
        new FieldSerializerChainBuilder(eventDataType, serializerGenerated)
            .getInstance(eventDataType);
    TypeSpec.Builder writerType =
        TypeSpec.anonymousClassBuilder("$S", eventDataType)
            .addSuperinterface(
                ParameterizedTypeName.get(
                    ClassName.get(AbstractObjectWriter.class), TypeName.get(eventDataType)))
            .addMethod(
                MethodSpec.methodBuilder("newSerializer")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PROTECTED)
                    .returns(
                        ParameterizedTypeName.get(
                            ClassName.get(JsonSerializer.class), TypeVariableName.get("?")))
                    .addCode("return ")
                    .addCode(instance)
                    .addCode(";\n")
                    .build());

    return builder.add("$L", writerType.build()).build();
  }

  public CodeBlock getEventDeserializer(TypeMirror eventDataType) {
    CodeBlock.Builder builder = CodeBlock.builder();
    boolean deserializerGenerated = !shouldGenerateDeserializer(eventDataType);
    CodeBlock instance =
        new FieldDeserializersChainBuilder(eventDataType, deserializerGenerated)
            .getInstance(eventDataType);
    TypeSpec.Builder readerType =
        TypeSpec.anonymousClassBuilder("$S", eventDataType)
            .addSuperinterface(
                ParameterizedTypeName.get(
                    ClassName.get(AbstractObjectReader.class), TypeName.get(eventDataType)))
            .addMethod(
                MethodSpec.methodBuilder("newDeserializer")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PROTECTED)
                    .returns(
                        ParameterizedTypeName.get(
                            ClassName.get(JsonDeserializer.class), TypeName.get(eventDataType)))
                    .addCode("return ")
                    .addCode(instance)
                    .addCode(";")
                    .build());

    return builder.add("$L", readerType.build()).build();
  }

  private boolean shouldGenerateSerializer(TypeMirror requestBeanType) {
    return !(processorUtil.isPrimitive(requestBeanType)
        || processorUtil.isPrimitiveArray(requestBeanType)
        || processorUtil.isArray(requestBeanType)
        || processorUtil.is2dArray(requestBeanType)
        || processorUtil.isCollection(requestBeanType)
        || processorUtil.isIterable(requestBeanType)
        || processorUtil.isEnum(requestBeanType)
        || processorUtil.isMap(requestBeanType)
        || processorUtil.isStringType(requestBeanType)
        || isWrapperType(requestBeanType)
        || isSerializer(requestBeanType));
  }

  /**
   * @param type the type
   * @return true if the type is one of the wrapper classes, false otherwise
   */
  public boolean isWrapperType(TypeMirror type) {
    return processorUtil.isAssignableFrom(type, Byte.class)
        || processorUtil.isAssignableFrom(type, Short.class)
        || processorUtil.isAssignableFrom(type, Integer.class)
        || processorUtil.isAssignableFrom(type, Long.class)
        || processorUtil.isAssignableFrom(type, Float.class)
        || processorUtil.isAssignableFrom(type, Double.class)
        || processorUtil.isAssignableFrom(type, Date.class)
        || processorUtil.isAssignableFrom(type, BigDecimal.class)
        || processorUtil.isAssignableFrom(type, Character.class)
        || processorUtil.isAssignableFrom(type, Boolean.class);
  }

  private boolean isSerializer(TypeMirror requestBeanType) {
    return nonNull(requestBeanType.getAnnotation(JSONMapper.class))
        || nonNull(requestBeanType.getAnnotation(JSONWriter.class));
  }

  private boolean shouldGenerateDeserializer(TypeMirror requestBeanType) {
    return !(processorUtil.isPrimitive(requestBeanType)
        || processorUtil.isPrimitiveArray(requestBeanType)
        || processorUtil.isArray(requestBeanType)
        || processorUtil.is2dArray(requestBeanType)
        || processorUtil.isCollection(requestBeanType)
        || processorUtil.isIterable(requestBeanType)
        || processorUtil.isEnum(requestBeanType)
        || processorUtil.isMap(requestBeanType)
        || processorUtil.isStringType(requestBeanType)
        || isWrapperType(requestBeanType)
        || isDeserializer(requestBeanType));
  }

  private boolean isDeserializer(TypeMirror requestBeanType) {
    return nonNull(requestBeanType.getAnnotation(JSONMapper.class))
        || nonNull(requestBeanType.getAnnotation(JSONReader.class));
  }

  private boolean isNullOrEmpty(String value) {
    return isNull(value) || value.trim().isEmpty();
  }
}
