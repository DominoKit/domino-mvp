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
package org.dominokit.domino.apt.client.processors.module.client.presenters;

import com.squareup.javapoet.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import org.dominokit.domino.api.client.mvp.PresenterConfig;
import org.dominokit.domino.api.client.mvp.ViewablePresenterConfig;
import org.dominokit.domino.api.client.mvp.presenter.PresenterSupplier;
import org.dominokit.domino.api.client.mvp.presenter.ViewBaseClientPresenter;
import org.dominokit.domino.api.client.mvp.presenter.ViewablePresenterSupplier;
import org.dominokit.domino.api.client.mvp.view.View;
import org.dominokit.domino.apt.commons.AbstractSourceBuilder;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;

public class PresenterConfigSourceWriter extends AbstractSourceBuilder {
  private final Element presenterElement;

  public PresenterConfigSourceWriter(
      Element presenterElement, ProcessingEnvironment processingEnv) {
    super(processingEnv);
    this.presenterElement = presenterElement;
  }

  @Override
  public List<TypeSpec.Builder> asTypeBuilder() {
    TypeSpec.Builder configType =
        DominoTypeBuilder.classBuilder(
            presenterElement.getSimpleName().toString() + "_Config", PresenterProcessor.class);
    ClassName _self =
        ClassName.bestGuess(
            elements.getPackageOf(presenterElement).getQualifiedName().toString()
                + "."
                + presenterElement.getSimpleName().toString()
                + "_Config");

    if (processorUtil.isAssignableFrom(presenterElement, ViewBaseClientPresenter.class)) {
      processorUtil
          .findTypeArgument(presenterElement.asType(), View.class)
          .ifPresent(
              viewTypeMirror -> {
                ParameterizedTypeName presenterSupplier =
                    ParameterizedTypeName.get(
                        ClassName.get(ViewablePresenterSupplier.class),
                        TypeName.get(presenterElement.asType()),
                        TypeName.get(viewTypeMirror));
                ParameterizedTypeName viewSupplier =
                    ParameterizedTypeName.get(
                        ClassName.get(Supplier.class), TypeName.get(viewTypeMirror));
                configType
                    .addSuperinterface(
                        ParameterizedTypeName.get(
                            ClassName.get(ViewablePresenterConfig.class),
                            TypeName.get(presenterElement.asType()),
                            TypeName.get(viewTypeMirror)))
                    .addField(
                        FieldSpec.builder(
                                presenterSupplier,
                                "presenterSupplier",
                                Modifier.PRIVATE,
                                Modifier.STATIC)
                            .build())
                    .addField(
                        FieldSpec.builder(
                                viewSupplier, "viewSupplier", Modifier.PRIVATE, Modifier.STATIC)
                            .build())
                    .addMethod(
                        MethodSpec.methodBuilder("setPresenterSupplier")
                            .addModifiers(Modifier.PUBLIC)
                            .returns(TypeName.VOID)
                            .addParameter(presenterSupplier, "presenterSupplier")
                            .addStatement("$T.presenterSupplier = presenterSupplier", _self)
                            .build())
                    .addMethod(
                        MethodSpec.methodBuilder("getPresenterSupplier")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(presenterSupplier)
                            .addStatement("return $T.presenterSupplier", _self)
                            .build())
                    .addMethod(
                        MethodSpec.methodBuilder("setViewSupplier")
                            .addModifiers(Modifier.PUBLIC)
                            .returns(TypeName.VOID)
                            .addParameter(viewSupplier, "viewSupplier")
                            .addStatement("$T.viewSupplier = viewSupplier", _self)
                            .build())
                    .addMethod(
                        MethodSpec.methodBuilder("getViewSupplier")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(viewSupplier)
                            .addStatement("return $T.viewSupplier", _self)
                            .build());
              });
    } else {
      ParameterizedTypeName presenterSupplier =
          ParameterizedTypeName.get(
              ClassName.get(PresenterSupplier.class), TypeName.get(presenterElement.asType()));
      configType
          .addSuperinterface(
              ParameterizedTypeName.get(
                  ClassName.get(PresenterConfig.class), TypeName.get(presenterElement.asType())))
          .addField(
              FieldSpec.builder(
                      presenterSupplier, "presenterSupplier", Modifier.PRIVATE, Modifier.STATIC)
                  .build())
          .addMethod(
              MethodSpec.methodBuilder("setPresenterSupplier")
                  .addModifiers(Modifier.PUBLIC)
                  .returns(TypeName.VOID)
                  .addParameter(presenterSupplier, "presenterSupplier")
                  .addStatement("$T.presenterSupplier = presenterSupplier", _self)
                  .build())
          .addMethod(
              MethodSpec.methodBuilder("getPresenterSupplier")
                  .addAnnotation(Override.class)
                  .addModifiers(Modifier.PUBLIC)
                  .returns(presenterSupplier)
                  .addStatement("return $T.presenterSupplier", _self)
                  .build());
    }

    return Collections.singletonList(configType);
  }
}
