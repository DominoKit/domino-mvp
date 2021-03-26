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
package org.dominokit.domino.api.server.logging;

import static org.dominokit.domino.api.server.logging.SerializableLogRecordMapper.asLogRecord;

import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.dominokit.domino.api.shared.logging.SerializableLogRecord;

public class RemoteLoggingHandler implements Handler<RoutingContext> {

  private final RemoteLogger logger;

  public RemoteLoggingHandler(RemoteLogger logger) {
    this.logger = logger;
  }

  @Override
  public void handle(RoutingContext event) {
    SerializableLogRecord serializableLogRecord =
        Json.decodeValue(event.getBody().toString(), SerializableLogRecord.class);
    logger.log(asLogRecord(serializableLogRecord), serializableLogRecord.permutationStrongName);
    event.response().end();
  }
}
