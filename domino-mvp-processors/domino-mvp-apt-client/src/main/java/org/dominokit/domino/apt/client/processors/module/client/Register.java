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

import static java.util.Objects.isNull;

import java.io.*;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class Register {

  private Set<String> items;
  private final Messager messager;
  private final ProcessingEnvironment processingEnv;
  private final String fileName;

  public Register(
      String fileName, Set<String> items, Messager messager, ProcessingEnvironment processingEnv) {

    this.items = items;
    this.messager = messager;
    this.processingEnv = processingEnv;
    this.fileName = fileName;
  }

  public Set<String> readItems() {
    if (isNull(items)) {
      items = new TreeSet<>();
      try {
        FileObject resource =
            processingEnv
                .getFiler()
                .getResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/domino/" + fileName);
        new BufferedReader(new InputStreamReader(resource.openInputStream()))
            .lines()
            .forEach(items::add);
      } catch (IOException notFoundException) {
        messager.printMessage(
            Diagnostic.Kind.NOTE, "File not found 'META-INF/domino/" + fileName + "'");
      }
    }

    return items;
  }

  public void writeItems() {
    try {
      FileObject resource =
          processingEnv
              .getFiler()
              .createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/domino/" + fileName);
      PrintWriter out = new PrintWriter(new OutputStreamWriter(resource.openOutputStream()));
      items.forEach(out::println);
      out.close();
    } catch (IOException ex) {
      messager.printMessage(Diagnostic.Kind.ERROR, ExceptionUtils.getStackTrace(ex));
    }
  }
}
