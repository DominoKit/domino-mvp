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
package org.dominokit.domino.apt.server.handlers;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.dominokit.domino.api.server.resource.ResourceRegistry;
import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;
import org.dominokit.domino.apt.commons.ProcessorElement;

public class ResourceRegisterMethodWriter
    extends AbstractRegisterMethodWriter<ResourceEntry, ProcessorElement> {

  public ResourceRegisterMethodWriter(TypeSpec.Builder serverModuleTypeBuilder) {
    super(serverModuleTypeBuilder);
  }

  @Override
  protected String methodName() {
    return "registerResources";
  }

  @Override
  protected Class<?> registryClass() {
    return ResourceRegistry.class;
  }

  @Override
  protected void registerItem(ResourceEntry entry, MethodSpec.Builder methodBuilder) {
    methodBuilder.addStatement(
        "registry.registerResource($L.class)", TypeName.get(entry.resource.getElement().asType()));
  }

  @Override
  protected ResourceEntry parseEntry(ProcessorElement handler) {
    return new ResourceEntry(handler);
  }
}
