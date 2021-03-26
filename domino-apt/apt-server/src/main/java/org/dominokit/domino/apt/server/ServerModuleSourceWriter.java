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

import com.google.auto.service.AutoService;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.List;
import org.dominokit.domino.api.server.config.ServerModule;
import org.dominokit.domino.api.server.config.ServerModuleConfiguration;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;
import org.dominokit.domino.apt.commons.JavaSourceWriter;
import org.dominokit.domino.apt.commons.ProcessorElement;
import org.dominokit.domino.apt.server.handlers.ResourceRegisterMethodWriter;

public class ServerModuleSourceWriter extends JavaSourceWriter {

  private final List<ProcessorElement> resources;

  public ServerModuleSourceWriter(
      ProcessorElement serverModuleElement, List<ProcessorElement> resources) {
    super(serverModuleElement);
    this.resources = resources;
  }

  @Override
  public String write() throws IOException {
    AnnotationSpec autoServiceAnnotation =
        AnnotationSpec.builder(AutoService.class)
            .addMember("value", "$T.class", ServerModuleConfiguration.class)
            .build();
    TypeSpec.Builder serverModuleTypeBuilder =
        DominoTypeBuilder.classBuilder(
                processorElement.getAnnotation(ServerModule.class).name() + "ServerModule",
                ServerModuleAnnotationProcessor.class)
            .addAnnotation(autoServiceAnnotation)
            .addSuperinterface(ServerModuleConfiguration.class);

    writeRegisterMethods(serverModuleTypeBuilder);

    StringBuilder asString = new StringBuilder();
    JavaFile.builder(processorElement.elementPackage(), serverModuleTypeBuilder.build())
        .skipJavaLangImports(true)
        .build()
        .writeTo(asString);
    return asString.toString();
  }

  private void writeRegisterMethods(TypeSpec.Builder serverModuleTypeBuilder) {
    new ResourceRegisterMethodWriter(serverModuleTypeBuilder).write(resources);
  }
}
