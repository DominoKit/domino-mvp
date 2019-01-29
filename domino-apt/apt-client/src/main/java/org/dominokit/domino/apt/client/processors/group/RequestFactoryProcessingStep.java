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
package org.dominokit.domino.apt.client.processors.group;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.dominokit.domino.api.client.annotations.AutoRoute;
import org.dominokit.domino.api.client.annotations.ListenTo;
import org.dominokit.domino.apt.client.processors.module.client.presenters.DominoEventListenerSourceWriter;
import org.dominokit.domino.apt.client.processors.module.client.presenters.HistoryStartupTaskSourceWriter;
import org.dominokit.domino.apt.client.processors.module.client.presenters.PresenterCommandSourceWriter;
import org.dominokit.domino.apt.client.processors.module.client.presenters.PresenterConfigSourceWriter;
import org.dominokit.domino.apt.commons.AbstractProcessingStep;
import org.dominokit.domino.apt.commons.ExceptionUtil;
import org.dominokit.domino.apt.commons.StepBuilder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

public class RequestFactoryProcessingStep extends AbstractProcessingStep {


    public RequestFactoryProcessingStep(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public static class Builder extends StepBuilder<RequestFactoryProcessingStep> {

        public RequestFactoryProcessingStep build() {
            return new RequestFactoryProcessingStep(processingEnv);
        }
    }


    public void process(
            Set<? extends Element> elementsByAnnotation) {

        for (Element element : elementsByAnnotation) {
            try {
                generateFactory(element);
            } catch (Exception e) {
                ExceptionUtil.messageStackTrace(messager, e);
            }
        }

    }

    private void generateFactory(Element serviceElement) {
        TypeSpec.Builder typeBuilder = new RequestFactorySourceWriter(serviceElement, processingEnv).asTypeBuilder();

        JavaFile javaFile = JavaFile.builder(elements.getPackageOf(serviceElement).getQualifiedName().toString(), typeBuilder.build()).build();
        writeSource(javaFile);
    }

}
