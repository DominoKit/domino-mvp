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
package org.dominokit.domino.apt.client.processors.module.client.views;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import javax.lang.model.element.Modifier;
import org.dominokit.domino.api.client.mvp.ViewRegistry;
import org.dominokit.domino.api.client.mvp.view.LazyViewLoader;
import org.dominokit.domino.api.client.mvp.view.View;
import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;

public class RegisterViewsMethodWriter extends AbstractRegisterMethodWriter<ViewEntry, String> {

  public RegisterViewsMethodWriter(TypeSpec.Builder clientModuleTypeBuilder) {
    super(clientModuleTypeBuilder);
  }

  @Override
  protected String methodName() {
    return "registerViews";
  }

  @Override
  protected Class<?> registryClass() {
    return ViewRegistry.class;
  }

  @Override
  protected void registerItem(ViewEntry entry, MethodSpec.Builder methodBuilder) {
    MethodSpec makeMethod =
        MethodSpec.methodBuilder("make")
            .addModifiers(Modifier.PROTECTED)
            .addAnnotation(Override.class)
            .returns(View.class)
            .addStatement("return new $T()", ClassName.bestGuess(entry.view))
            .build();
    TypeSpec lazyLoaderType =
        TypeSpec.anonymousClassBuilder(
                "$T.class.getCanonicalName()", ClassName.bestGuess(entry.presenter))
            .superclass(LazyViewLoader.class)
            .addMethod(makeMethod)
            .build();
    methodBuilder.addStatement("registry.registerView($L)", lazyLoaderType);
  }

  @Override
  protected ViewEntry parseEntry(String presenter) {
    String[] viewPair = presenter.split(":");
    return new ViewEntry(viewPair[0], viewPair[1]);
  }
}
