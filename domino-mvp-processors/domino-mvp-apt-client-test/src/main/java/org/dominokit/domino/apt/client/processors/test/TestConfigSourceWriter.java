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
package org.dominokit.domino.apt.client.processors.test;

import static java.util.Objects.nonNull;

import com.squareup.javapoet.*;
import io.vertx.ext.unit.TestContext;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import org.dominokit.domino.api.client.ModuleConfiguration;
import org.dominokit.domino.api.client.annotations.ClientModule;
import org.dominokit.domino.api.client.annotations.presenter.PresenterProxy;
import org.dominokit.domino.api.client.annotations.presenter.Singleton;
import org.dominokit.domino.api.client.mvp.presenter.PresenterSupplier;
import org.dominokit.domino.api.client.mvp.presenter.ViewBaseClientPresenter;
import org.dominokit.domino.api.client.mvp.presenter.ViewablePresenterSupplier;
import org.dominokit.domino.apt.commons.AbstractSourceBuilder;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;
import org.dominokit.domino.test.api.client.*;
import org.dominokit.domino.test.api.client.annotations.*;
import org.dominokit.domino.test.api.client.annotations.FakeView;

public class TestConfigSourceWriter extends AbstractSourceBuilder {

  private final Element testClassElement;

  public TestConfigSourceWriter(Element testClassElement, ProcessingEnvironment processingEnv) {
    super(processingEnv);
    this.testClassElement = testClassElement;
  }

  @Override
  public List<TypeSpec.Builder> asTypeBuilder() {
    String testConfigClassName = testClassElement.getSimpleName().toString() + "_Config";

    TypeSpec.Builder testConfigType =
        DominoTypeBuilder.classBuilder(testConfigClassName, TestConfigProcessor.class)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(DominoTestConfig.class);

    generateModulesMethod(testConfigType);
    generateOnBeforeClientStartMethod(testConfigType);
    generateOnClientStartedMethod(testConfigType);
    generateBindSpiesMethod(testConfigType);
    generateBindFakeViewsMethod(testConfigType);

    return Collections.singletonList(testConfigType);
  }

  private void generateModulesMethod(TypeSpec.Builder testConfigType) {
    List<TypeMirror> modules =
        processorUtil.getClassArrayValueFromAnnotation(
            testClassElement, TestConfig.class, "modules");

    MethodSpec.Builder methodBuilder =
        MethodSpec.methodBuilder("getModules")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(
                ParameterizedTypeName.get(
                    ClassName.get(List.class), TypeName.get(ModuleConfiguration.class)))
            .addStatement(
                "$T<$T> modules = new $T<>()",
                TypeName.get(List.class),
                TypeName.get(ModuleConfiguration.class),
                TypeName.get(ArrayList.class));

    modules.forEach(
        typeMirror ->
            methodBuilder.addStatement("modules.add(new $T())", TypeName.get(typeMirror)));

    if (nonNull(testClassElement.getAnnotation(ClientModule.class))) {
      String moduleName = testClassElement.getAnnotation(ClientModule.class).name();
      addModuleByBestGuess(methodBuilder, moduleName);
    }

    if (nonNull(testClassElement.getAnnotation(TestModules.class))) {
      String[] testModules = testClassElement.getAnnotation(TestModules.class).value();
      Arrays.stream(testModules)
          .forEach(moduleName -> addModuleByBestGuess(methodBuilder, moduleName));
    }

    methodBuilder.addStatement("return modules");

    testConfigType.addMethod(methodBuilder.build());
  }

  private void addModuleByBestGuess(MethodSpec.Builder methodBuilder, String moduleName) {
    methodBuilder.addStatement(
        "modules.add(new $T())",
        ClassName.bestGuess(
            elements.getPackageOf(testClassElement).getQualifiedName().toString()
                + "."
                + (moduleName + "ModuleConfiguration")));
  }

  private void generateOnBeforeClientStartMethod(TypeSpec.Builder testConfigType) {

    MethodSpec.Builder methodBuilder =
        MethodSpec.methodBuilder("onBeforeClientStart")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(TypeName.VOID)
            .addParameter(TypeName.get(DominoTestCase.class), "dominoTestCase")
            .addParameter(TypeName.get(ClientContext.class), "clientContext")
            .addParameter(TypeName.get(TestContext.class), "testContext")
            .addStatement(
                "$T test = ($T) dominoTestCase",
                testClassElement.asType(),
                testClassElement.asType());

    testClassElement.getEnclosedElements().stream()
        .filter(element -> nonNull(element.getAnnotation(OnBeforeClientStart.class)))
        .filter(element -> ElementKind.METHOD.equals(element.getKind()))
        .filter(
            element ->
                element.getModifiers().contains(Modifier.PUBLIC)
                    || element.getModifiers().contains(Modifier.DEFAULT))
        .forEach(
            element -> {
              String args = getElementArguments(element);

              methodBuilder.addStatement(
                  "test." + element.getSimpleName().toString() + "(" + args + ")");
            });

    testConfigType.addMethod(methodBuilder.build());
  }

  private void generateOnClientStartedMethod(TypeSpec.Builder testConfigType) {

    MethodSpec.Builder methodBuilder =
        MethodSpec.methodBuilder("onClientStarted")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(TypeName.VOID)
            .addParameter(TypeName.get(DominoTestCase.class), "dominoTestCase")
            .addParameter(TypeName.get(ClientContext.class), "clientContext")
            .addParameter(TypeName.get(TestContext.class), "testContext")
            .addStatement(
                "$T test = ($T) dominoTestCase",
                testClassElement.asType(),
                testClassElement.asType());

    testClassElement.getEnclosedElements().stream()
        .filter(element -> nonNull(element.getAnnotation(OnClientStarted.class)))
        .filter(element -> ElementKind.METHOD.equals(element.getKind()))
        .filter(
            element ->
                element.getModifiers().contains(Modifier.PUBLIC)
                    || element.getModifiers().contains(Modifier.DEFAULT))
        .forEach(
            element -> {
              String args = getElementArguments(element);

              methodBuilder.addStatement(
                  "test." + element.getSimpleName().toString() + "(" + args + ")");
            });

    testConfigType.addMethod(methodBuilder.build());
  }

  private String getElementArguments(Element element) {
    ExecutableElement executableElement = (ExecutableElement) element;
    if (executableElement.getParameters().size() > 0) {
      return executableElement.getParameters().stream()
          .filter(param -> isClientContext(param) || isTestContext(param))
          .map(param -> getParamName(param))
          .collect(Collectors.joining(", "));
    } else {
      return "";
    }
  }

  private String getParamName(VariableElement param) {
    if (isClientContext(param)) return "clientContext";
    else if (isTestContext(param)) return "testContext";
    return param.getSimpleName().toString();
  }

  private boolean isClientContext(VariableElement param) {
    return types.isSameType(
        param.asType(), elements.getTypeElement(ClientContext.class.getCanonicalName()).asType());
  }

  private boolean isTestContext(VariableElement param) {
    return types.isSameType(
        param.asType(), elements.getTypeElement(TestContext.class.getCanonicalName()).asType());
  }

  private void generateBindSpiesMethod(TypeSpec.Builder testConfigType) {

    MethodSpec.Builder methodBuilder =
        MethodSpec.methodBuilder("bindSpies")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(TypeName.VOID)
            .addParameter(TypeName.get(DominoTestCase.class), "dominoTestCase")
            .addStatement(
                "$T test = ($T) dominoTestCase",
                testClassElement.asType(),
                testClassElement.asType());

    testClassElement.getEnclosedElements().stream()
        .filter(element -> nonNull(element.getAnnotation(PresenterSpy.class)))
        .filter(element -> ElementKind.FIELD.equals(element.getKind()))
        .forEach(
            element -> {
              methodBuilder.addCode(generateSpyBinding(element));
            });

    testConfigType.addMethod(methodBuilder.build());
  }

  private CodeBlock generateSpyBinding(Element element) {
    Optional<TypeMirror> targetPresenter =
        processorUtil.getClassValueFromAnnotation(element, PresenterSpy.class, "value");
    if (targetPresenter.isPresent()) {

      CodeBlock.Builder builder = CodeBlock.builder();

      TypeMirror presenterType = targetPresenter.get();
      Element presenterTypeElement = types.asElement(presenterType);

      String configName = getBindingConfigName(presenterTypeElement);

      boolean isViewable =
          processorUtil.isAssignableFrom(presenterTypeElement, ViewBaseClientPresenter.class);

      TypeName supplierType;
      if (isViewable) {
        supplierType = TypeName.get(ViewablePresenterSupplier.class);
      } else {
        supplierType = TypeName.get(PresenterSupplier.class);
      }

      ClassName configType =
          ClassName.bestGuess(
              elements.getPackageOf(presenterTypeElement).toString() + "." + configName);
      builder.addStatement(
          "$T $L = new $T()", configType, processorUtil.smallFirstLetter(configName), configType);
      builder.beginControlFlow(
          "$L.setPresenterSupplier(new $T<>($L, () ->",
          processorUtil.smallFirstLetter(configName),
          supplierType,
          nonNull(presenterTypeElement.getAnnotation(Singleton.class)));
      builder.addStatement(
          "test.$L = new $T()",
          element.getSimpleName().toString(),
          ClassName.bestGuess(
              elements
                      .getPackageOf(types.asElement(targetPresenter.get()))
                      .getQualifiedName()
                      .toString()
                  + "."
                  + types.asElement(element.asType()).getSimpleName().toString()));
      builder.addStatement("return test.$L", element.getSimpleName().toString());
      builder.endControlFlow("))");

      return builder.build();
    }
    return CodeBlock.builder().build();
  }

  private void generateBindFakeViewsMethod(TypeSpec.Builder testConfigType) {

    MethodSpec.Builder methodBuilder =
        MethodSpec.methodBuilder("bindFakeViews")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(TypeName.VOID)
            .addParameter(TypeName.get(DominoTestCase.class), "dominoTestCase")
            .addStatement(
                "$T test = ($T) dominoTestCase",
                testClassElement.asType(),
                testClassElement.asType());

    testClassElement.getEnclosedElements().stream()
        .filter(element -> nonNull(element.getAnnotation(FakeView.class)))
        .filter(element -> ElementKind.FIELD.equals(element.getKind()))
        .forEach(
            element -> {
              methodBuilder.addCode(generateFakeViewsBinding(element));
            });

    testConfigType.addMethod(methodBuilder.build());
  }

  private CodeBlock generateFakeViewsBinding(Element element) {
    Optional<TypeMirror> targetPresenter =
        processorUtil.getClassValueFromAnnotation(element, FakeView.class, "value");
    if (targetPresenter.isPresent()) {

      CodeBlock.Builder builder = CodeBlock.builder();

      TypeMirror presenterType = targetPresenter.get();
      Element presenterTypeElement = types.asElement(presenterType);

      String configName = getBindingConfigName(presenterTypeElement);

      ClassName configType =
          ClassName.bestGuess(
              elements.getPackageOf(presenterTypeElement).toString() + "." + configName);
      builder.addStatement(
          "$T $L = new $T()", configType, processorUtil.smallFirstLetter(configName), configType);
      builder.beginControlFlow(
          "$L.setViewSupplier(() ->", processorUtil.smallFirstLetter(configName));
      builder.addStatement(
          "test.$L = new $T()", element.getSimpleName().toString(), element.asType());
      builder.addStatement("return test.$L", element.getSimpleName().toString());
      builder.endControlFlow(")");

      return builder.build();
    }
    return CodeBlock.builder().build();
  }

  private String getBindingConfigName(Element presenterTypeElement) {
    String configName;
    if (nonNull(presenterTypeElement.getAnnotation(PresenterProxy.class))) {
      configName = presenterTypeElement.getSimpleName().toString() + "_Presenter_Config";
    } else {
      configName = presenterTypeElement.getSimpleName().toString() + "_Config";
    }
    return configName;
  }
}
