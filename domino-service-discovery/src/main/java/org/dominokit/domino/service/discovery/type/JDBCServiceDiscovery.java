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
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.JDBCDataSource;
import java.util.function.Function;
import org.dominokit.domino.service.discovery.configuration.DataSourceServiceConfiguration;

public class JDBCServiceDiscovery {
  private final ServiceDiscovery serviceDiscovery;

  public JDBCServiceDiscovery(ServiceDiscovery serviceDiscovery) {
    this.serviceDiscovery = serviceDiscovery;
  }

  public void publish(
      DataSourceServiceConfiguration configuration, Handler<AsyncResult<Record>> handler) {
    serviceDiscovery.publish(createJDBCRecord(configuration), handler);
  }

  private Record createJDBCRecord(DataSourceServiceConfiguration configuration) {
    return JDBCDataSource.createRecord(
        configuration.getName(), configuration.getLocation(), configuration.getMetadata());
  }

  public void getClient(
      Function<Record, Boolean> filter, Handler<AsyncResult<JDBCClient>> handler) {
    JDBCDataSource.getJDBCClient(serviceDiscovery, filter, handler);
  }

  public void getClient(
      Function<Record, Boolean> filter,
      JsonObject configuration,
      Handler<AsyncResult<JDBCClient>> handler) {
    JDBCDataSource.getJDBCClient(serviceDiscovery, filter, configuration, handler);
  }

  public void getClient(JsonObject jsonFilter, Handler<AsyncResult<JDBCClient>> handler) {
    JDBCDataSource.getJDBCClient(serviceDiscovery, jsonFilter, handler);
  }

  public void getClient(
      JsonObject jsonFilter, JsonObject configuration, Handler<AsyncResult<JDBCClient>> handler) {
    JDBCDataSource.getJDBCClient(serviceDiscovery, jsonFilter, configuration, handler);
  }
}
