/*
 * Copyright © 2019 Dominokit
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

import com.google.auto.service.AutoService;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import javax.lang.model.element.Modifier;
import org.dominokit.domino.api.client.ModuleConfiguration;
import org.dominokit.domino.api.client.ModuleConfigurationProvider;
import org.dominokit.domino.api.client.annotations.ClientModule;
import org.dominokit.domino.api.shared.compile.GwtIncompatible;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;
import org.dominokit.domino.apt.commons.ProcessorElement;

public class ConfigurationProviderSourceWriter {

  private final ProcessorElement element;

  public ConfigurationProviderSourceWriter(ProcessorElement element) {
    this.element = element;
  }

  public String write() throws IOException {
    AnnotationSpec autoService =
        AnnotationSpec.builder(AutoService.class)
            .addMember("value", "ModuleConfigurationProvider.class")
            .build();
    AnnotationSpec gwtIncompatible =
        AnnotationSpec.builder(GwtIncompatible.class)
            .addMember("value", "\"Unused in GWT compilation\"")
            .build();

    TypeSpec configurationProvider =
        DominoTypeBuilder.classBuilder(
                element.getAnnotation(ClientModule.class).name() + "ModuleConfiguration_Provider",
                ConfigurationProviderAnnotationProcessor.class)
            .addAnnotation(gwtIncompatible)
            .addAnnotation(autoService)
            .addSuperinterface(ModuleConfigurationProvider.class)
            .addMethod(makeGetMethod())
            .build();

    JavaFile javaFile =
        JavaFile.builder(element.elementPackage(), configurationProvider)
            .skipJavaLangImports(true)
            .build();

    StringBuilder classAsString = new StringBuilder();
    javaFile.writeTo(classAsString);
    return classAsString.toString();
  }

  private MethodSpec makeGetMethod() {
    return MethodSpec.methodBuilder("get")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .returns(ModuleConfiguration.class)
        .addStatement(
            "return new "
                + element.getAnnotation(ClientModule.class).name()
                + "ModuleConfiguration()")
        .build();
  }
}
