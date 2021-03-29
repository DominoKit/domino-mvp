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
package org.dominokit.domino.service.discovery.type;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.MessageSource;
import java.util.function.Function;
import org.dominokit.domino.service.discovery.configuration.MessageSourceConfiguration;

public class MessageSourceServiceDiscovery {
  private final ServiceDiscovery serviceDiscovery;

  public MessageSourceServiceDiscovery(ServiceDiscovery serviceDiscovery) {
    this.serviceDiscovery = serviceDiscovery;
  }

  public void publish(
      MessageSourceConfiguration configuration, Handler<AsyncResult<Record>> handler) {
    serviceDiscovery.publish(createMessageSourceRecord(configuration), handler);
  }

  private Record createMessageSourceRecord(MessageSourceConfiguration configuration) {
    return MessageSource.createRecord(
        configuration.getName(),
        configuration.getAddress(),
        configuration.getMessageType(),
        configuration.getMetadata());
  }

  public <T> void getConsumer(
      Function<Record, Boolean> filter, Handler<AsyncResult<MessageConsumer<T>>> handler) {
    MessageSource.getConsumer(serviceDiscovery, filter, handler);
  }

  public <T> void getConsumer(
      JsonObject jsonFilter, Handler<AsyncResult<MessageConsumer<T>>> handler) {
    MessageSource.getConsumer(serviceDiscovery, jsonFilter, handler);
  }
}
