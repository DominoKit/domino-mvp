/*
 * Copyright © 2018 The GWT Authors
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
package org.dominokit.domino.apt.client.processors.aggregate;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.dominokit.domino.api.client.annotations.*;
import org.dominokit.domino.apt.client.processors.module.client.presenters.DominoEventListenerSourceWriter;
import org.dominokit.domino.apt.client.processors.module.client.presenters.HistoryStartupTaskSourceWriter;
import org.dominokit.domino.apt.client.processors.module.client.presenters.PresenterCommandSourceWriter;
import org.dominokit.domino.apt.commons.AbstractProcessingStep;
import org.dominokit.domino.apt.commons.ExceptionUtil;
import org.dominokit.domino.apt.commons.StepBuilder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class AggregateProcessingStep extends AbstractProcessingStep {


    public AggregateProcessingStep(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public static class Builder extends StepBuilder<AggregateProcessingStep> {

        public AggregateProcessingStep build() {
            return new AggregateProcessingStep(processingEnv);
        }
    }

    public void process(
            Set<? extends Element> elementsByAnnotation) {

        for (Element element : elementsByAnnotation) {
            try {
                if(element.getKind().equals(ElementKind.METHOD)){
                    ExecutableElement methodElement = (ExecutableElement) element;
                    generateAggregate(methodElement);
                }
            } catch (Exception e) {
                ExceptionUtil.messageStackTrace(messager, e);
            }
        }

    }

    private void generateAggregate(ExecutableElement methodElement) {
        TypeSpec.Builder typeBuilder = new AggregateSourceWriter(methodElement, processingEnv)
                .asTypeBuilder();

        JavaFile javaFile = JavaFile.builder(elements.getPackageOf(methodElement.getEnclosingElement()).getQualifiedName().toString(), typeBuilder.build()).build();

        writeSource(javaFile);
    }


}
