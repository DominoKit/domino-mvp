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
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.dominokit.domino.api.client.annotations.ClientModule;
import org.dominokit.domino.apt.commons.BaseProcessor;
import org.dominokit.domino.apt.commons.ProcessorElement;

@AutoService(Processor.class)
public class ConfigurationProviderAnnotationProcessor extends BaseProcessor {

  private static final Logger LOGGER =
      Logger.getLogger(ConfigurationProviderAnnotationProcessor.class.getName());

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Set<? extends Element> modules = roundEnv.getElementsAnnotatedWith(ClientModule.class);
    //        modules.forEach(m -> generateModuleConfigurationProvider(newProcessorElement(m)));
    return false;
  }

  private void generateModuleConfigurationProvider(ProcessorElement element) {
    try (Writer sourceWriter =
        obtainSourceWriter(
            element.elementPackage(),
            element.getAnnotation(ClientModule.class).name() + "ModuleConfiguration_Provider")) {

      String clazz = new ConfigurationProviderSourceWriter(element).write();
      sourceWriter.write(clazz);
      sourceWriter.flush();
    } catch (Exception e) {
      messager.printMessage(
          Diagnostic.Kind.ERROR, "could not generate class " + ExceptionUtils.getStackTrace(e));
    }
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> annotations = new HashSet<>();
    annotations.add(ClientModule.class.getCanonicalName());
    return annotations;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }
}
