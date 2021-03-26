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
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.ws.rs.Path;
import org.dominokit.domino.api.server.config.ServerModule;
import org.dominokit.domino.apt.commons.BaseProcessor;
import org.dominokit.domino.apt.commons.ProcessorElement;

@AutoService(Processor.class)
public class ServerModuleAnnotationProcessor extends BaseProcessor {

  private static final Logger LOGGER =
      Logger.getLogger(ServerModuleAnnotationProcessor.class.getName());

  private final Set<String> supportedAnnotations = new HashSet<>();

  public ServerModuleAnnotationProcessor() {
    supportedAnnotations.add(Path.class.getCanonicalName());
    supportedAnnotations.add(ServerModule.class.getCanonicalName());
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return supportedAnnotations;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (roundEnv.getElementsAnnotatedWith(ServerModule.class).size() == 1) {
      ProcessorElement module =
          getModule(
              roundEnv.getElementsAnnotatedWith(ServerModule.class).stream()
                  .findFirst()
                  .orElseThrow(IllegalArgumentException::new));

      generateServerModule(module, getResources(roundEnv));
    } else if (roundEnv.getElementsAnnotatedWith(ServerModule.class).size() > 1) {
      this.messager.printMessage(
          Diagnostic.Kind.ERROR, "Only one ServerModule is allowed per project.!");
    }
    return true;
  }

  private ProcessorElement getModule(Element element) {
    return newProcessorElement(element);
  }

  private List<ProcessorElement> getResources(RoundEnvironment roundEnv) {
    return roundEnv.getElementsAnnotatedWith(Path.class).stream()
        .filter(element -> ElementKind.CLASS.equals(element.getKind()))
        .map(this::newProcessorElement)
        .collect(Collectors.toList());
  }

  private void generateServerModule(
      ProcessorElement processorElement, List<ProcessorElement> resources) {
    try (Writer sourceWriter =
        obtainSourceWriter(
            processorElement.elementPackage(),
            processorElement.getAnnotation(ServerModule.class).name() + "ServerModule")) {
      sourceWriter.write(new ServerModuleSourceWriter(processorElement, resources).write());
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not generate classes : ", e);
      messager.printMessage(Diagnostic.Kind.ERROR, "could not generate class");
    }
  }
}
