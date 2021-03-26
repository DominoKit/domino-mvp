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
package org.dominokit.domino.plugins.resteasy.security;

import com.google.auto.service.AutoService;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import org.dominokit.domino.api.server.security.NoAuthFilter;
import org.dominokit.domino.api.server.security.RequiresPermissions;
import org.dominokit.domino.api.server.security.RequiresUser;
import org.dominokit.domino.plugins.resteasy.AppGlobals;

@Provider
@AutoService(Providers.class)
public class AuthorizationFilterFeature implements DynamicFeature {

  private static List<Class<? extends Annotation>> filterAnnotations =
      Collections.unmodifiableList(
          Arrays.asList(RequiresPermissions.class, RequiresUser.class, RolesAllowed.class));

  @Override
  public void configure(ResourceInfo resourceInfo, FeatureContext context) {
    if (AppGlobals.get().getConfig().getBoolean("security.filters.enabled", false)) {

      List<Annotation> authzSpecs = new ArrayList<>();
      for (Class<? extends Annotation> annotationClass : filterAnnotations) {
        Annotation classAuthzSpec = resourceInfo.getResourceClass().getAnnotation(annotationClass);
        Annotation methodAuthzSpec =
            resourceInfo.getResourceMethod().getAnnotation(annotationClass);

        if (classAuthzSpec != null) authzSpecs.add(classAuthzSpec);
        if (methodAuthzSpec != null) authzSpecs.add(methodAuthzSpec);

        if (resourceInfo.getResourceClass().isAnnotationPresent(NoAuthFilter.class)
            || resourceInfo.getResourceMethod().isAnnotationPresent(NoAuthFilter.class)) return;
      }

      if (!authzSpecs.isEmpty()) {
        context.register(new AuthorizationFilter(authzSpecs), Priorities.AUTHORIZATION);
      }
    }
  }
}
