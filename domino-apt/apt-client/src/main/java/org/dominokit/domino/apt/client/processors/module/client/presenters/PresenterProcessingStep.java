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
package org.dominokit.domino.apt.client.processors.module.client.presenters;

import org.dominokit.domino.api.client.annotations.presenter.AutoRoute;
import org.dominokit.domino.api.client.annotations.presenter.ListenTo;
import org.dominokit.domino.apt.commons.AbstractProcessingStep;
import org.dominokit.domino.apt.commons.ExceptionUtil;
import org.dominokit.domino.apt.commons.StepBuilder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

public class PresenterProcessingStep extends AbstractProcessingStep {


    public PresenterProcessingStep(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    public static class Builder extends StepBuilder<PresenterProcessingStep> {

        public PresenterProcessingStep build() {
            return new PresenterProcessingStep(processingEnv);
        }
    }


    public void process(
            Set<? extends Element> elementsByAnnotation) {

        for (Element element : elementsByAnnotation) {
            try {
                generateConfig(element);
                generateCommand(element);
                generateEventListeners(element);
                generateRouter(element);
            } catch (Exception e) {
                ExceptionUtil.messageStackTrace(messager, e);
            }
        }

    }

    private void generateConfig(Element presenterElement) {

        writeSource(new PresenterConfigSourceWriter(presenterElement, processingEnv).asTypeBuilder(), elements.getPackageOf(presenterElement).getQualifiedName().toString());
    }


    private void generateRouter(Element presenterElement) {
        AutoRoute autoRoute = presenterElement.getAnnotation(AutoRoute.class);
        if (nonNull(autoRoute)) {
            generateHistoryStartupTask(autoRoute.token(), presenterElement);
        }
    }

    private void generateHistoryStartupTask(String token, Element presenterElement) {
        writeSource(new HistoryStartupTaskSourceWriter(token, presenterElement, processingEnv)
                .asTypeBuilder(), elements.getPackageOf(presenterElement).getQualifiedName().toString().replace("presenters", "routing"));
    }

    private void generateEventListeners(Element presenterElement) {
        List<Element> listeners = processorUtil.getAnnotatedMethods(presenterElement.asType(), ListenTo.class);

        listeners.forEach(listenerElement -> generateEventListener(listenerElement, presenterElement));
    }


    private void generateEventListener(Element listenerElement, Element presenterElement) {
        writeSource(new DominoEventListenerSourceWriter(presenterElement, listenerElement, processingEnv)
                .asTypeBuilder(), elements.getPackageOf(presenterElement).getQualifiedName().toString().replace("presenters", "listeners"));
    }


    private void generateCommand(Element presenterElement) {
        writeSource(new PresenterCommandSourceWriter(presenterElement, processingEnv).asTypeBuilder(), elements.getPackageOf(presenterElement).getQualifiedName().toString());
    }

}
