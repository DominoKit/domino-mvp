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

import org.dominokit.domino.apt.commons.BaseProcessor;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

//@AutoService(Processor.class)
public class AggregateProcessor extends BaseProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }

//    @Override
//    protected Iterable<? extends BasicAnnotationProcessor.ProcessingStep> initSteps() {
//        return ImmutableList.of(
//                new AggregateProcessingStep.Builder()
//                        .setProcessingEnv(processingEnv)
//                        .classBuilder());
//    }
}
