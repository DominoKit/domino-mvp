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
package org.dominokit.domino.plugins.resteasy.resteasy;

import io.vertx.core.Context;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import java.io.IOException;
import java.util.List;
import org.dominokit.domino.plugins.resteasy.spi.Plugin;
import org.jboss.resteasy.core.SynchronousDispatcher;
import org.jboss.resteasy.plugins.server.embedded.SecurityDomain;
import org.jboss.resteasy.plugins.server.vertx.RequestDispatcher;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

public class PluginRequestDispatcher extends RequestDispatcher {

  private List<Plugin> plugins;

  public PluginRequestDispatcher(
      SynchronousDispatcher dispatcher,
      ResteasyProviderFactory providerFactory,
      SecurityDomain domain,
      List<Plugin> plugins) {
    super(dispatcher, providerFactory, domain);
    this.plugins = plugins;
  }

  @Override
  public void service(
      Context context,
      HttpServerRequest req,
      HttpServerResponse resp,
      HttpRequest vertxReq,
      HttpResponse vertxResp,
      boolean handleNotFound)
      throws IOException {
    service(0, context, req, resp, vertxReq, vertxResp, handleNotFound);
  }

  private void service(
      int i,
      Context context,
      HttpServerRequest req,
      HttpServerResponse resp,
      HttpRequest vertxReq,
      HttpResponse vertxResp,
      boolean handleNotFound)
      throws IOException {
    if (i < plugins.size())
      plugins
          .get(i)
          .aroundRequest(
              vertxReq,
              () -> service(i + 1, context, req, resp, vertxReq, vertxResp, handleNotFound));
    else super.service(context, req, resp, vertxReq, vertxResp, handleNotFound);
  }
}
