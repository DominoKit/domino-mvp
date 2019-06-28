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
package org.dominokit.domino.apt.client.processors.module.client;

import org.dominokit.domino.apt.commons.AbstractProcessingStep;
import org.dominokit.domino.apt.commons.ExceptionUtil;
import org.dominokit.domino.apt.commons.StepBuilder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.Set;

public class ClientModuleProcessingStep extends AbstractProcessingStep {

    private Set<String> presenters;
    private Set<String> views;
    private Set<String> initialTasks;

    public ClientModuleProcessingStep(
            Set<String> presenters, Set<String> views,
            Set<String> initialTasks,
            ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);

        this.presenters = presenters;
        this.views = views;
        this.initialTasks = initialTasks;
    }

    public static class Builder extends StepBuilder<ClientModuleProcessingStep> {

        private Set<String> presenters;
        private Set<String> views;
        private Set<String> initialTasks;

        public Builder setPresenters(Set<String> presenters) {
            this.presenters = presenters;
            return this;
        }

        public Builder setViews(Set<String> views) {
            this.views = views;
            return this;
        }

        public Builder setInitialTasks(Set<String> initialTasks) {
            this.initialTasks = initialTasks;
            return this;
        }

        public ClientModuleProcessingStep build() {
            return new ClientModuleProcessingStep(presenters, views, initialTasks, processingEnv);
        }
    }

    public void process(Set<? extends Element> elementsByAnnotation) {
        elementsByAnnotation
                .stream()
                .filter(element -> ElementKind.CLASS.equals(element.getKind()))
                .forEach(element -> {
                    try {
                        writeSource(new ModuleConfigurationSourceWriter(element, presenters, views, initialTasks, processingEnv)
                                .asTypeBuilder(), elements.getPackageOf(element).getQualifiedName().toString());
                    } catch (Exception e) {
                        ExceptionUtil.messageStackTrace(messager, e, element);
                    }
                });
    }
}
