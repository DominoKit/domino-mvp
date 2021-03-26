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
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.EventBusService;
import java.util.function.Function;
import org.dominokit.domino.service.discovery.configuration.EventBusServiceConfiguration;

public class EventBusServiceDiscovery {
  private final ServiceDiscovery serviceDiscovery;

  public EventBusServiceDiscovery(ServiceDiscovery serviceDiscovery) {
    this.serviceDiscovery = serviceDiscovery;
  }

  public void publish(
      EventBusServiceConfiguration configuration, Handler<AsyncResult<Record>> handler) {
    serviceDiscovery.publish(createEventBusRecord(configuration), handler);
  }

  private Record createEventBusRecord(EventBusServiceConfiguration configuration) {
    return EventBusService.createRecord(
        configuration.getName(),
        configuration.getAddress(),
        configuration.getServiceClass(),
        configuration.getMetadata());
  }

  public <T> void getProxy(Class<T> serviceClass, Handler<AsyncResult<T>> handler) {
    EventBusService.getProxy(serviceDiscovery, serviceClass, handler);
  }

  public <T> void getServiceProxy(
      Function<Record, Boolean> filter, Class<T> serviceClass, Handler<AsyncResult<T>> handler) {
    EventBusService.getServiceProxy(serviceDiscovery, filter, serviceClass, handler);
  }

  public <T> void getServiceProxy(
      JsonObject jsonFilter, Class<T> serviceClass, Handler<AsyncResult<T>> handler) {
    EventBusService.getServiceProxyWithJsonFilter(
        serviceDiscovery, jsonFilter, serviceClass, handler);
  }
}
