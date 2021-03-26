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
package org.dominokit.domino.service.discovery.type;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;
import java.util.function.Function;
import org.dominokit.domino.service.discovery.configuration.HttpEndpointConfiguration;

public class HttpServiceDiscovery {
  private final ServiceDiscovery serviceDiscovery;

  public HttpServiceDiscovery(ServiceDiscovery serviceDiscovery) {
    this.serviceDiscovery = serviceDiscovery;
  }

  public void publish(
      HttpEndpointConfiguration configuration, Handler<AsyncResult<Record>> handler) {
    serviceDiscovery.publish(createHttpEndpointRecord(configuration), handler);
  }

  private Record createHttpEndpointRecord(HttpEndpointConfiguration configuration) {
    return HttpEndpoint.createRecord(
        configuration.getName(),
        configuration.isSsl(),
        configuration.getHost(),
        configuration.getPort(),
        configuration.getRoot(),
        configuration.getMetadata());
  }

  public void getHttpClient(
      Function<Record, Boolean> filter, Handler<AsyncResult<HttpClient>> handler) {
    HttpEndpoint.getClient(serviceDiscovery, filter, handler);
  }

  public void getHttpClient(
      Function<Record, Boolean> filter,
      JsonObject configuration,
      Handler<AsyncResult<HttpClient>> handler) {
    HttpEndpoint.getClient(serviceDiscovery, filter, configuration, handler);
  }

  public void getHttpClient(JsonObject jsonFilter, Handler<AsyncResult<HttpClient>> handler) {
    HttpEndpoint.getClient(serviceDiscovery, jsonFilter, handler);
  }

  public void getHttpClient(
      JsonObject jsonFilter, JsonObject configuration, Handler<AsyncResult<HttpClient>> handler) {
    HttpEndpoint.getClient(serviceDiscovery, jsonFilter, configuration, handler);
  }

  public void getWebClient(
      Function<Record, Boolean> filter, Handler<AsyncResult<WebClient>> handler) {
    HttpEndpoint.getWebClient(serviceDiscovery, filter, handler);
  }

  public void getWebClient(
      Function<Record, Boolean> filter,
      JsonObject configuration,
      Handler<AsyncResult<WebClient>> handler) {
    HttpEndpoint.getWebClient(serviceDiscovery, filter, configuration, handler);
  }

  public void getWebClient(JsonObject jsonFilter, Handler<AsyncResult<WebClient>> handler) {
    HttpEndpoint.getWebClient(serviceDiscovery, jsonFilter, handler);
  }

  public void getWebClient(
      JsonObject jsonFilter, JsonObject configuration, Handler<AsyncResult<WebClient>> handler) {
    HttpEndpoint.getWebClient(serviceDiscovery, jsonFilter, configuration, handler);
  }
}
