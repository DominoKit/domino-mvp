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
import io.vertx.redis.RedisClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.RedisDataSource;
import java.util.function.Function;
import org.dominokit.domino.service.discovery.configuration.DataSourceServiceConfiguration;

public class RedisServiceDiscovery {
  private final ServiceDiscovery serviceDiscovery;

  public RedisServiceDiscovery(ServiceDiscovery serviceDiscovery) {
    this.serviceDiscovery = serviceDiscovery;
  }

  public void publish(
      DataSourceServiceConfiguration configuration, Handler<AsyncResult<Record>> handler) {
    serviceDiscovery.publish(createRedisRecord(configuration), handler);
  }

  private Record createRedisRecord(DataSourceServiceConfiguration configuration) {
    return RedisDataSource.createRecord(
        configuration.getName(), configuration.getLocation(), configuration.getMetadata());
  }

  public void getClient(
      Function<Record, Boolean> filter, Handler<AsyncResult<RedisClient>> handler) {
    RedisDataSource.getRedisClient(serviceDiscovery, filter, handler);
  }

  public void getClient(
      Function<Record, Boolean> filter,
      JsonObject configuration,
      Handler<AsyncResult<RedisClient>> handler) {
    RedisDataSource.getRedisClient(serviceDiscovery, filter, configuration, handler);
  }

  public void getClient(JsonObject jsonFilter, Handler<AsyncResult<RedisClient>> handler) {
    RedisDataSource.getRedisClient(serviceDiscovery, jsonFilter, handler);
  }

  public void getClient(
      JsonObject jsonFilter, JsonObject configuration, Handler<AsyncResult<RedisClient>> handler) {
    RedisDataSource.getRedisClient(serviceDiscovery, jsonFilter, configuration, handler);
  }
}
