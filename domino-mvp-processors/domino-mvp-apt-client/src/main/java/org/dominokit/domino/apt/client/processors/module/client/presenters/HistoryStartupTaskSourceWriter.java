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

import static java.util.Objects.nonNull;

import com.squareup.javapoet.*;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.dominokit.domino.api.client.annotations.StartupTask;
import org.dominokit.domino.api.client.annotations.presenter.*;
import org.dominokit.domino.api.client.events.BaseRoutingAggregator;
import org.dominokit.domino.api.client.events.DefaultEventAggregator;
import org.dominokit.domino.api.client.startup.BaseNoTokenRoutingStartupTask;
import org.dominokit.domino.api.client.startup.BaseRoutingStartupTask;
import org.dominokit.domino.apt.client.processors.module.client.presenters.model.DependsOnModel;
import org.dominokit.domino.apt.client.processors.module.client.presenters.model.EventsGroup;
import org.dominokit.domino.apt.commons.AbstractSourceBuilder;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;
import org.dominokit.domino.apt.commons.ExceptionUtil;
import org.dominokit.domino.history.DominoHistory;
import org.dominokit.domino.history.TokenFilter;

public class HistoryStartupTaskSourceWriter extends AbstractSourceBuilder {

  private final String token;
  private final Element presenterElement;

  protected HistoryStartupTaskSourceWriter(
      String token, Element presenterElement, ProcessingEnvironment processingEnv) {
    super(processingEnv);
    this.token = token;
    this.presenterElement = presenterElement;
  }

  @Override
  public List<TypeSpec.Builder> asTypeBuilder() {
    String taskClassName = presenterElement.getSimpleName() + "HistoryListenerTask";
    TypeSpec.Builder taskType =
        DominoTypeBuilder.classBuilder(taskClassName, PresenterProcessor.class);
    taskType
        .addAnnotation(StartupTask.class)
        .superclass(
            TypeName.get(
                hasToken() ? BaseRoutingStartupTask.class : BaseNoTokenRoutingStartupTask.class))
        .addMethod(
            MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addCode(getSuperCall(taskType))
                .build());

    if (hasParent()) {
      taskType.addMethod(getParentMethod());
    }

    if (hasToken()) {
      taskType.addMethod(getFilterTokenMethod()).addMethod(getStartupFilterTokenMethod());
    }
    taskType.addMethod(onStateReadyMethod());

    if (processorUtil.findClassAnnotation(presenterElement, AutoRoute.class).routeOnce()) {
      taskType.addMethod(routOnceMethod());
    }

    if (processorUtil.findClassAnnotation(presenterElement, AutoRoute.class).reRouteActivated()) {
      taskType.addMethod(reRouteActivatedMethod());
    }

    return Collections.singletonList(taskType);
  }

  private boolean hasToken() {
    return nonNull(token) && !token.isEmpty();
  }

  private boolean hasParent() {
    Presenter presenter = presenterElement.getAnnotation(Presenter.class);
    String parent = presenter.parent();
    return nonNull(parent) && !parent.trim().isEmpty();
  }

  private MethodSpec getParentMethod() {
    MethodSpec.Builder method =
        MethodSpec.methodBuilder("getParent")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PROTECTED)
            .returns(ParameterizedTypeName.get(Optional.class, String.class))
            .addStatement(
                "return $T.of($S)",
                Optional.class,
                presenterElement.getAnnotation(Presenter.class).parent());
    return method.build();
  }

  private MethodSpec routOnceMethod() {
    MethodSpec.Builder method =
        MethodSpec.methodBuilder("isRoutingOnce")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PROTECTED)
            .returns(TypeName.BOOLEAN)
            .addStatement("return true");
    return method.build();
  }

  private MethodSpec reRouteActivatedMethod() {
    MethodSpec.Builder method =
        MethodSpec.methodBuilder("isReRouteActivated")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PROTECTED)
            .returns(TypeName.BOOLEAN)
            .addStatement("return true");
    return method.build();
  }

  private MethodSpec getStartupFilterTokenMethod() {

    MethodSpec.Builder method =
        MethodSpec.methodBuilder("getStartupTokenFilter")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PROTECTED)
            .returns(TokenFilter.class);

    Optional<String> tokenFilterMethodName =
        getTokenFilterMethodName(presenterElement, StartupTokenFilter.class);
    if (tokenFilterMethodName.isPresent()) {
      method.addStatement(
          "return $T." + tokenFilterMethodName.get() + "(\"" + token + "\")",
          TypeName.get(presenterElement.asType()));
    } else {
      method.addStatement(
          "return $T."
              + (token.trim().isEmpty() ? "any()" : "startsWithPathFilter(\"" + token + "\")"),
          TypeName.get(TokenFilter.class));
    }

    return method.build();
  }

  private MethodSpec getFilterTokenMethod() {

    MethodSpec.Builder method =
        MethodSpec.methodBuilder("getTokenFilter")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PROTECTED)
            .returns(TokenFilter.class);

    Optional<String> tokenFilterMethodName =
        getTokenFilterMethodName(presenterElement, RoutingTokenFilter.class);
    if (tokenFilterMethodName.isPresent()) {
      method.addStatement(
          "return $T." + tokenFilterMethodName.get() + "(\"" + token + "\")",
          TypeName.get(presenterElement.asType()));
    } else {
      method.addStatement(
          "return $T."
              + (token.trim().isEmpty() ? "any()" : "endsWithPathFilter(\"" + token + "\")"),
          TypeName.get(TokenFilter.class));
    }

    return method.build();
  }

  private CodeBlock getSuperCall(TypeSpec.Builder taskType) {
    DependsOnModel dependsOnModel = getDependsOnModel();
    CodeBlock.Builder codeBlock = CodeBlock.builder();
    if (dependsOnModel.getEventsGroups().isEmpty()) {
      codeBlock.addStatement(
          "super($T.asList(new $T()))",
          TypeName.get(Arrays.class),
          TypeName.get(DefaultEventAggregator.class));
    } else {
      String superCall =
          "super($T.asList("
              + dependsOnModel.getEventsGroups().stream()
                  .map(
                      eventsGroup ->
                          "new "
                              + "EventsAggregator_"
                              + dependsOnModel.getEventsGroups().indexOf(eventsGroup)
                              + "()")
                  .collect(Collectors.joining(","))
              + "))";

      codeBlock.addStatement(superCall, TypeName.get(Arrays.class));
      dependsOnModel
          .getEventsGroups()
          .forEach(
              eventsGroup -> {
                TypeSpec.Builder innerAggregator =
                    TypeSpec.classBuilder(
                        "EventsAggregator_"
                            + dependsOnModel.getEventsGroups().indexOf(eventsGroup));
                innerAggregator
                    .addModifiers(Modifier.PRIVATE)
                    .addModifiers(Modifier.STATIC)
                    .superclass(BaseRoutingAggregator.class)
                    .addMethod(
                        MethodSpec.constructorBuilder()
                            .addModifiers(Modifier.PRIVATE)
                            .addStatement(
                                "super(Arrays.asList("
                                    + eventsGroup.getClassList().stream()
                                        .map(typeMirror -> "$T.class")
                                        .collect(Collectors.joining(","))
                                    + "))",
                                eventsGroup.getClassList().toArray())
                            .build());

                taskType.addType(innerAggregator.build());
              });
    }
    return codeBlock.build();
  }

  private MethodSpec onStateReadyMethod() {

    MethodSpec.Builder onStateReadyBuilder =
        MethodSpec.methodBuilder("onStateReady")
            .addAnnotation(Override.class)
            .returns(TypeName.VOID)
            .addModifiers(Modifier.PROTECTED)
            .addParameter(TypeName.get(DominoHistory.State.class), "state");

    CodeBlock.Builder codeBlock = CodeBlock.builder();

    codeBlock.beginControlFlow(
        " new $T().onPresenterReady(presenter ->", ClassName.bestGuess(makeRequestClassName()));

    codeBlock.addStatement("bindPresenter(presenter)");

    List<Element> routingState =
        processorUtil.getAnnotatedFields(presenterElement.asType(), RoutingState.class);
    List<Element> pathParameters =
        processorUtil.getAnnotatedFields(presenterElement.asType(), PathParameter.class);
    List<Element> fragmentParameters =
        processorUtil.getAnnotatedFields(presenterElement.asType(), FragmentParameter.class);
    List<Element> queryParameters =
        processorUtil.getAnnotatedFields(presenterElement.asType(), QueryParameter.class);

    if (!routingState.isEmpty()
        || !pathParameters.isEmpty()
        || !fragmentParameters.isEmpty()
        || !queryParameters.isEmpty()) {
      codeBlock.addStatement("presenter.setState(state)");
    }

    String[] revealConditionMethods = revealConditionMethods();
    if (revealConditionMethods.length > 0) {
      String methodsCondition =
          Arrays.stream(revealConditionMethods)
              .map(method -> "presenter.$L()")
              .collect(Collectors.joining(" && "));
      codeBlock.beginControlFlow("if(" + methodsCondition + ")", revealConditionMethods);
    }

    List<Element> onRoutingMethods =
        processorUtil.getAnnotatedMethods(presenterElement.asType(), OnRouting.class);

    onRoutingMethods.forEach(
        element -> codeBlock.addStatement("presenter.$L()", element.getSimpleName().toString()));

    if (nonNull(processorUtil.findClassAnnotation(presenterElement, AutoReveal.class))) {
      codeBlock.beginControlFlow("if(!presenter.isActivated())");
      codeBlock.addStatement("presenter.reveal()");
      codeBlock.endControlFlow();
    }

    if (revealConditionMethods.length > 0) {
      codeBlock.endControlFlow();
    }

    codeBlock.endControlFlow(").send()");

    onStateReadyBuilder.addCode(codeBlock.build());

    return onStateReadyBuilder.build();
  }

  private DependsOnModel getDependsOnModel() {
    return getDependsOnModel((TypeElement) types.asElement(presenterElement.asType()));
  }

  private DependsOnModel getDependsOnModel(TypeElement element) {
    DependsOnModel dependsOnModel = new DependsOnModel();
    try {
      TypeMirror superclass = element.getSuperclass();
      if (superclass.getKind().equals(TypeKind.NONE)) {
        return dependsOnModel;
      }

      if (nonNull(element.getAnnotation(DependsOn.class))) {
        List<? extends AnnotationMirror> annotations = element.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotations) {

          if (types.isSameType(
              annotationMirror.getAnnotationType(),
              elements.getTypeElement(DependsOn.class.getName()).asType())) {
            Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues =
                annotationMirror.getElementValues();

            elementValues.values().stream()
                .findFirst()
                .ifPresent(
                    annotationValue -> {
                      List<AnnotationValue> eventsGroupsAnnotations =
                          (List<AnnotationValue>) annotationValue.getValue();
                      eventsGroupsAnnotations.stream()
                          .forEach(
                              eventGroupAnnotationMirror -> {
                                AnnotationMirror eventGroupAnnMirror =
                                    (AnnotationMirror) eventGroupAnnotationMirror.getValue();

                                Collection<? extends AnnotationValue> values =
                                    eventGroupAnnMirror.getElementValues().values();
                                AnnotationValue groupValue = values.stream().findFirst().get();
                                List<AnnotationValue> eventTypes =
                                    (List<AnnotationValue>) groupValue.getValue();
                                Iterator<? extends AnnotationValue> iterator =
                                    eventTypes.iterator();

                                List<TypeMirror> eventTypesMirrors = new ArrayList<>();

                                while (iterator.hasNext()) {
                                  AnnotationValue next = iterator.next();
                                  eventTypesMirrors.add((TypeMirror) next.getValue());
                                }

                                if (!eventTypesMirrors.isEmpty()) {
                                  dependsOnModel.addEventGroup(new EventsGroup(eventTypesMirrors));
                                }
                              });
                    });
          }
        }
        return dependsOnModel;
      } else {
        return getDependsOnModel((TypeElement) types.asElement(element.getSuperclass()));
      }
    } catch (Exception error) {
      ExceptionUtil.messageStackTrace(messager, error, element);
    }
    return dependsOnModel;
  }

  private String[] revealConditionMethods() {
    List<Element> revealConditionMethods =
        processorUtil.getAnnotatedMethods(presenterElement.asType(), RevealCondition.class);
    if (nonNull(revealConditionMethods) && !revealConditionMethods.isEmpty()) {
      String[] methods = new String[revealConditionMethods.size()];
      revealConditionMethods.stream()
          .map(element -> element.getSimpleName().toString())
          .collect(Collectors.toList())
          .toArray(methods);
      return methods;
    }
    return new String[] {};
  }

  public Optional<String> getTokenFilterMethodName(
      Element targetElement, Class<? extends Annotation> annotation) {
    Optional<String> method =
        targetElement.getEnclosedElements().stream()
            .filter(
                e ->
                    e.getKind().equals(ElementKind.METHOD)
                        && e.getModifiers().contains(Modifier.STATIC)
                        && nonNull(e.getAnnotation(annotation)))
            .map(element -> element.getSimpleName().toString())
            .findFirst();
    TypeMirror superclass = ((TypeElement) targetElement).getSuperclass();
    if (superclass.getKind().equals(TypeKind.NONE)) {
      return method;
    } else {
      return method.isPresent()
          ? method
          : getTokenFilterMethodName(types.asElement(superclass), annotation);
    }
  }

  private String makeRequestClassName() {
    return elements.getPackageOf(presenterElement).getQualifiedName().toString()
        + "."
        + presenterElement.getSimpleName().toString()
        + "Command";
  }
}
