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
package org.dominokit.domino.api.server.response;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import java.util.List;
import org.dominokit.domino.rest.shared.request.ResponseBean;

public class VertxResponseContext<S extends ResponseBean> implements ResponseContext<S> {

  private final RoutingContext routingContext;

  public VertxResponseContext(RoutingContext routingContext) {
    this.routingContext = routingContext;
  }

  @Override
  public ResponseContext<S> putHeader(String name, String value) {
    routingContext.response().putHeader(name, value);
    return this;
  }

  @Override
  public ResponseContext<S> putHeader(String name, Iterable<String> values) {
    routingContext.response().putHeader(name, values);
    return this;
  }

  @Override
  public ResponseContext<S> statusCode(int statusCode) {
    routingContext.response().setStatusCode(statusCode);
    return this;
  }

  @Override
  public void end() {
    routingContext.response().end();
  }

  @Override
  public void end(S body) {
    putHeader("Content-Type", "application/json");
    end(Json.encode(body));
  }

  @Override
  public void end(String body) {
    routingContext.response().end(body);
  }

  @Override
  public void end(S[] bodyArray) {
    putHeader("Content-Type", "application/json");
    end(Json.encode(bodyArray));
  }

  @Override
  public void end(List<S> bodyList) {
    putHeader("Content-Type", "application/json");
    end(Json.encode(bodyList));
  }

  @Override
  public void endHandler(ResponseEndHandler endHandler) {
    routingContext.response().endHandler(event -> endHandler.onResponseEnded());
  }
}
