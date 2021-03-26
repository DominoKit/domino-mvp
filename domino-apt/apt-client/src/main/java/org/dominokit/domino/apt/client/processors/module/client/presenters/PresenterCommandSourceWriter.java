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
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import org.dominokit.domino.api.client.annotations.presenter.Command;
import org.dominokit.domino.api.client.request.PresenterCommand;
import org.dominokit.domino.apt.commons.AbstractSourceBuilder;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;

public class PresenterCommandSourceWriter extends AbstractSourceBuilder {
  private final Element presenterElement;

  public PresenterCommandSourceWriter(
      Element presenterElement, ProcessingEnvironment processingEnv) {
    super(processingEnv);
    this.presenterElement = presenterElement;
  }

  @Override
  public List<TypeSpec.Builder> asTypeBuilder() {
    return Collections.singletonList(
        DominoTypeBuilder.classBuilder(
                presenterElement.getSimpleName().toString() + "Command", PresenterProcessor.class)
            .addAnnotation(Command.class)
            .superclass(
                ParameterizedTypeName.get(
                    ClassName.get(PresenterCommand.class), TypeName.get(presenterElement.asType())))
            .addMethod(
                MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement(
                        "configure(new $T())",
                        ClassName.bestGuess(
                            elements.getPackageOf(presenterElement).getQualifiedName().toString()
                                + "."
                                + presenterElement.getSimpleName().toString()
                                + "_Config"))
                    .build()));
  }
}
