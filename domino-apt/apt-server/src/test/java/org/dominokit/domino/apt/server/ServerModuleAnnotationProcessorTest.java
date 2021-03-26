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
package org.dominokit.domino.apt.server;

import static org.dominokit.domino.test.apt.ProcessorAssert.assertProcessing;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.processing.Processor;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class ServerModuleAnnotationProcessorTest {

  public static final String BASE_PACKAGE = "org/dominokit/domino/apt/server/";

  private Processor serverModuleAnnotationProcessor;

  @Before
  public void setUp() throws Exception {
    serverModuleAnnotationProcessor = new ServerModuleAnnotationProcessor();
  }

  private String getExpectedResultFileContent(String resourceName) throws IOException {
    try (InputStream resourceInputStream =
        this.getClass().getResourceAsStream("results/" + resourceName)) {
      return IOUtils.toString(resourceInputStream, "UTF-8");
    }
  }

  @Test
  public void
      givenServerModuleAnnotationProcessor_whenCompilingClassThatIsNotAnnotatedWithAnyServerModuleAnnotation_thenClassShouldBeCompiledSuccessfully()
          throws Exception {
    assertProcessing(BASE_PACKAGE + "NoneServerModuleClass.java")
        .withProcessor(serverModuleAnnotationProcessor)
        .compilesWithoutErrors();
  }

  @Test
  public void
      givenAClassAnnotatedAsHandlerWithoutImplementingHandlerInterface_whenCompiling_thenCompileShouldFail()
          throws Exception {
    assertProcessing(
            BASE_PACKAGE + "noHandlers/HandlerNotImplementingRequestHandlerInterface.java",
            BASE_PACKAGE + "package-info.java")
        .withProcessor(serverModuleAnnotationProcessor)
        .failsToCompile();
  }
}
