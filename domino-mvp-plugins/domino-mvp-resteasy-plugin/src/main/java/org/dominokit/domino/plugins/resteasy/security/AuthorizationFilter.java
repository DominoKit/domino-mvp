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
package org.dominokit.domino.plugins.resteasy.security;

import io.reactivex.Single;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import org.dominokit.domino.api.server.security.AuthorizationException;
import org.dominokit.domino.api.server.security.RequiresPermissions;
import org.dominokit.domino.api.server.security.RequiresUser;
import org.jboss.resteasy.core.interception.jaxrs.PreMatchContainerRequestContext;

public class AuthorizationFilter implements ContainerRequestFilter {
  private final Map<AuthorizingAnnotationHandler, Annotation> authzChecks;

  public AuthorizationFilter(Collection<Annotation> authzSpecs) {
    Map<AuthorizingAnnotationHandler, Annotation> authChecks = new HashMap<>(authzSpecs.size());
    for (Annotation authSpec : authzSpecs) {
      authChecks.put(createHandler(authSpec), authSpec);
    }
    this.authzChecks = Collections.unmodifiableMap(authChecks);
  }

  private static AuthorizingAnnotationHandler createHandler(Annotation annotation) {
    Class<?> t = annotation.annotationType();
    if (RequiresPermissions.class.equals(t)) return new VertxPermissionAnnotationHandler();
    else if (RequiresUser.class.equals(t)) return new VertxUserAnnotationHandler();
    else if (RolesAllowed.class.equals(t)) return new VertxRolesAnnotationHandler();
    else
      throw new IllegalArgumentException(
          "Cannot create a handler for the unknown for annotation " + t);
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    Single<Boolean> ret = null;
    for (Map.Entry<AuthorizingAnnotationHandler, Annotation> authzCheck : authzChecks.entrySet()) {
      AuthorizingAnnotationHandler handler = authzCheck.getKey();
      Annotation authzSpec = authzCheck.getValue();
      Single<Boolean> check = handler.assertAuthorized(authzSpec);
      if (ret == null) ret = check;
      else ret = ret.zipWith(check, (a, b) -> a && b);
    }
    if (ret != null) {
      PreMatchContainerRequestContext context = (PreMatchContainerRequestContext) requestContext;
      context.suspend();
      ret.subscribe(
          result -> {
            if (result) context.resume();
            else context.resume(new AuthorizationException("Authorization failed"));
          },
          error -> {
            context.resume(error);
          });
    }
  }
}
