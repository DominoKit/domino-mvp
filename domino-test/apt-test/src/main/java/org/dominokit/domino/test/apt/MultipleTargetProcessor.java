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
package org.dominokit.domino.test.apt;

import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

import com.google.common.truth.Truth;
import com.google.testing.compile.CompileTester;
import com.google.testing.compile.JavaFileObjects;
import java.util.Arrays;
import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;

public class MultipleTargetProcessor implements BaseTargetProcessor {

  private final String[] inputClassesNames;
  private final Processor processor;
  private final Processor[] rest;

  public MultipleTargetProcessor(String[] inputClassesNames, Processor processor) {
    this.inputClassesNames = inputClassesNames;
    this.processor = processor;
    this.rest = new Processor[] {};
  }

  public MultipleTargetProcessor(
      String[] inputClassesNames, Processor processor, Processor[] rest) {

    this.inputClassesNames = inputClassesNames;
    this.processor = processor;
    this.rest = rest;
  }

  @Override
  public CompileTester.SuccessfulCompilationClause compilesWithoutErrors() {
    return Truth.assert_()
        .about(javaSources())
        .that(Arrays.asList(asJavaFileObjectsArray()))
        .processedWith(processor, rest)
        .compilesWithoutError();
  }

  @Override
  public CompileTester.UnsuccessfulCompilationClause failsToCompile() {
    return Truth.assert_()
        .about(javaSources())
        .that(Arrays.asList(asJavaFileObjectsArray()))
        .processedWith(processor, rest)
        .failsToCompile();
  }

  private JavaFileObject[] asJavaFileObjectsArray() {
    JavaFileObject[] result = new JavaFileObject[inputClassesNames.length];
    for (int i = 0; i < inputClassesNames.length; i++)
      result[i] = JavaFileObjects.forResource(inputClassesNames[i]);
    return result;
  }
}
