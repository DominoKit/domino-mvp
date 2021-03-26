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
package org.dominokit.domino.apt.client.processors.module.client.listeners;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.dominokit.domino.api.client.extension.DominoEventsRegistry;
import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;

public class RegisterListenersMethodWriter
    extends AbstractRegisterMethodWriter<ListenerEntry, String> {

  public RegisterListenersMethodWriter(TypeSpec.Builder clientModuleTypeBuilder) {
    super(clientModuleTypeBuilder);
  }

  @Override
  protected String methodName() {
    return "registerListeners";
  }

  @Override
  protected Class<?> registryClass() {
    return DominoEventsRegistry.class;
  }

  @Override
  protected void registerItem(ListenerEntry entry, MethodSpec.Builder methodBuilder) {
    methodBuilder.addStatement(
        "registry.addListener($T.class, new $T())",
        ClassName.bestGuess(entry.dominoEvent),
        ClassName.bestGuess(entry.listener));
  }

  @Override
  protected ListenerEntry parseEntry(String request) {
    String[] requestPair = request.split(":");
    return new ListenerEntry(requestPair[0], requestPair[1]);
  }
}
