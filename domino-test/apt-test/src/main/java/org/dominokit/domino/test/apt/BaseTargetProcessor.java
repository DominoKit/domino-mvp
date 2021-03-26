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

import com.google.testing.compile.CompileTester;
import com.google.testing.compile.JavaFileObjects;
import java.nio.charset.Charset;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

public interface BaseTargetProcessor {

  default void generates(String result, String... rest) {
    compilesWithoutErrors()
        .and()
        .generatesSources(
            JavaFileObjects.forSourceString("", result), asJavaFileObjectsArray(rest));
  }

  default JavaFileObject[] asJavaFileObjectsArray(String... rest) {
    JavaFileObject[] result = new JavaFileObject[rest.length];
    for (int i = 0; i < rest.length; i++) result[i] = JavaFileObjects.forSourceString("", rest[i]);
    return result;
  }

  CompileTester.SuccessfulCompilationClause compilesWithoutErrors();

  CompileTester.UnsuccessfulCompilationClause failsToCompile();

  default void generatesResource(String basePackage, String fileName, String content) {
    compilesWithoutErrors()
        .and()
        .generatesFileNamed(StandardLocation.SOURCE_OUTPUT, basePackage, fileName)
        .withStringContents(Charset.defaultCharset(), content);
  }
}
