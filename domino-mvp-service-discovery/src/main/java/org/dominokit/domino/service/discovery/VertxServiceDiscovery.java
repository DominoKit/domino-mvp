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
package org.dominokit.domino.service.discovery;

import static java.util.Objects.isNull;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import java.util.List;
import java.util.function.Function;
import org.dominokit.domino.service.discovery.type.*;

public class VertxServiceDiscovery {

  private ServiceDiscovery serviceDiscovery;

  public VertxServiceDiscovery(Vertx vertx) {
    this.serviceDiscovery = ServiceDiscovery.create(vertx);
  }

  public ServiceDiscovery serviceDiscovery() {
    return serviceDiscovery;
  }

  public HttpServiceDiscovery http() {
    return new HttpServiceDiscovery(serviceDiscovery);
  }

  public EventBusServiceDiscovery eventBus() {
    return new EventBusServiceDiscovery(serviceDiscovery);
  }

  public MessageSourceServiceDiscovery messageSource() {
    return new MessageSourceServiceDiscovery(serviceDiscovery);
  }

  public JDBCServiceDiscovery jdbc() {
    return new JDBCServiceDiscovery(serviceDiscovery);
  }

  public RedisServiceDiscovery redis() {
    return new RedisServiceDiscovery(serviceDiscovery);
  }

  public MongoServiceDiscovery mongo() {
    return new MongoServiceDiscovery(serviceDiscovery);
  }

  public void unpublish(Record record, Handler<AsyncResult<Void>> handler) {
    serviceDiscovery.unpublish(record.getRegistration(), handler);
  }

  public void publishRecord(Record record, Handler<AsyncResult<Record>> handler) {
    serviceDiscovery.publish(record, handler);
  }

  public void lookup(Function<Record, Boolean> filter, Handler<AsyncResult<Record>> handler) {
    lookup(filter, false, handler);
  }

  public void lookupIncludeOutOfService(
      Function<Record, Boolean> filter, Handler<AsyncResult<Record>> handler) {
    lookup(filter, true, handler);
  }

  private void lookup(
      Function<Record, Boolean> filter,
      boolean includeOutOfService,
      Handler<AsyncResult<Record>> handler) {
    if (isNull(filter)) throw new InvalidFilterException();
    serviceDiscovery.getRecord(filter, includeOutOfService, handler);
  }

  public void lookupByJson(JsonObject jsonFilter, Handler<AsyncResult<Record>> handler) {
    serviceDiscovery.getRecord(jsonFilter, handler);
  }

  public void lookupAll(
      Function<Record, Boolean> filter, Handler<AsyncResult<List<Record>>> handler) {
    lookupAll(filter, false, handler);
  }

  public void lookupAllIncludeOutOfService(
      Function<Record, Boolean> filter, Handler<AsyncResult<List<Record>>> handler) {
    lookupAll(filter, true, handler);
  }

  private void lookupAll(
      Function<Record, Boolean> filter,
      boolean includeOutOfService,
      Handler<AsyncResult<List<Record>>> handler) {
    if (isNull(filter)) throw new InvalidFilterException();
    serviceDiscovery.getRecords(filter, includeOutOfService, handler);
  }

  public void lookupAllByJson(JsonObject jsonFilter, Handler<AsyncResult<List<Record>>> handler) {
    serviceDiscovery.getRecords(jsonFilter, handler);
  }

  public ServiceReference lookupForAReference(Record record) {
    return serviceDiscovery.getReference(record);
  }

  public ServiceReference lookupForAReferenceWithConfiguration(
      Record record, JsonObject configuration) {
    return serviceDiscovery.getReferenceWithConfiguration(record, configuration);
  }

  public void releaseServiceObject(Object object) {
    ServiceDiscovery.releaseServiceObject(serviceDiscovery, object);
  }

  public static class InvalidFilterException extends RuntimeException {}
}
