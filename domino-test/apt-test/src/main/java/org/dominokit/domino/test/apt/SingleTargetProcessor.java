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

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

import com.google.common.truth.Truth;
import com.google.testing.compile.CompileTester;
import com.google.testing.compile.JavaFileObjects;
import javax.annotation.processing.Processor;

public class SingleTargetProcessor implements BaseTargetProcessor {

  private final String inputClassName;
  private final Processor processor;
  private final Processor[] rest;

  public SingleTargetProcessor(String inputClassName, Processor processor) {
    this.inputClassName = inputClassName;
    this.processor = processor;
    this.rest = new Processor[] {};
  }

  public SingleTargetProcessor(String inputClassName, Processor processor, Processor[] rest) {

    this.inputClassName = inputClassName;
    this.processor = processor;
    this.rest = rest;
  }

  @Override
  public CompileTester.SuccessfulCompilationClause compilesWithoutErrors() {
    return Truth.assert_()
        .about(javaSource())
        .that(JavaFileObjects.forResource(inputClassName))
        .processedWith(processor, rest)
        .compilesWithoutError();
  }

  @Override
  public CompileTester.UnsuccessfulCompilationClause failsToCompile() {
    return Truth.assert_()
        .about(javaSource())
        .that(JavaFileObjects.forResource(inputClassName))
        .processedWith(processor, rest)
        .failsToCompile();
  }
}
