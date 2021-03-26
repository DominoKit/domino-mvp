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
package org.dominokit.domino.api.server.plugins;

import com.google.auto.service.AutoService;
import io.vertx.core.Vertx;
import org.dominokit.domino.api.server.ServerApp;
import org.dominokit.domino.api.server.entrypoint.VertxContext;
import org.dominokit.domino.rest.VertxInstanceProvider;

@AutoService(VertxInstanceProvider.class)
public class ServerVertxInstanceProvider implements VertxInstanceProvider {

  private VertxContext getVertxContext() {
    return (VertxContext) ServerApp.make().serverContext();
  }

  @Override
  public Vertx getInstance() {
    return getVertxContext().vertx();
  }

  @Override
  public String getHost() {
    return getVertxContext().httpServerOptions().getHost();
  }

  @Override
  public int getPort() {
    return getVertxContext().httpServerOptions().getPort();
  }

  @Override
  public String getProtocol() {
    return getVertxContext().httpServerOptions().isSsl() ? "https" : "http";
  }
}
