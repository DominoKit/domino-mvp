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

import io.reactivex.Single;
import io.vertx.reactivex.ext.auth.User;
import java.lang.annotation.Annotation;
import javax.annotation.security.RolesAllowed;
import org.dominokit.domino.api.server.security.AuthorizationException;

public class VertxRolesAnnotationHandler extends AuthorizingAnnotationHandler {

  @Override
  public Single<Boolean> assertAuthorized(Annotation authzSpec) {
    if (authzSpec instanceof RolesAllowed) {
      User user = getUser();
      if (user == null) return Single.error(new AuthorizationException("User required"));
      Single<Boolean> ret = Single.just(true);
      for (String perm : ((RolesAllowed) authzSpec).value()) {
        ret = user.rxIsAuthorized(perm).zipWith(ret, (a, b) -> a && b);
      }
      return ret;
    }
    return Single.just(true);
  }
}
