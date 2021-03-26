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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import org.dominokit.domino.api.server.security.AuthenticationException;

@Provider
@AutoService(Providers.class)
public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationException> {

  @Override
  public Response toResponse(AuthenticationException exception) {
    return Response.status(Status.UNAUTHORIZED)
        .entity(exception.getMessage())
        .type(MediaType.TEXT_PLAIN_TYPE)
        .build();
  }
}
