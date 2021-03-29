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
package org.dominokit.domino.apt.client.processors.module.client.initialtasks;

import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import org.dominokit.domino.api.client.annotations.StartupTask;
import org.dominokit.domino.api.client.startup.ClientStartupTask;
import org.dominokit.domino.apt.commons.BaseProcessor;

public class InitialTasksCollector {

  private final BaseProcessor.ElementFactory elementFactory;
  private final Set<String> initialTasks;

  public InitialTasksCollector(
      BaseProcessor.ElementFactory elementFactory, Set<String> initialTasks) {
    this.elementFactory = elementFactory;
    this.initialTasks = initialTasks;
  }

  public void collectInitialTasks(RoundEnvironment roundEnv) {
    roundEnv.getElementsAnnotatedWith(StartupTask.class).stream()
        .map(elementFactory::make)
        .filter(e -> e.validateElementKind(ElementKind.CLASS))
        .filter(i -> i.isAssignableFrom(ClientStartupTask.class))
        .collect(Collectors.toSet())
        .forEach(i -> initialTasks.add(i.fullQualifiedNoneGenericName()));
  }
}
