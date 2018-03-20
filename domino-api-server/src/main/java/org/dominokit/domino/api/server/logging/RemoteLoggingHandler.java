package org.dominokit.domino.api.server.logging;

import org.dominokit.domino.api.shared.logging.SerializableLogRecord;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import static org.dominokit.domino.api.server.logging.SerializableLogRecordMapper.asLogRecord;

public class RemoteLoggingHandler implements Handler<RoutingContext> {

    private final RemoteLogger logger;

    public RemoteLoggingHandler(RemoteLogger logger) {
        this.logger = logger;
    }

    @Override
    public void handle(RoutingContext event) {
        SerializableLogRecord serializableLogRecord = Json.decodeValue(event.getBody().toString(), SerializableLogRecord.class);
        logger.log(asLogRecord(serializableLogRecord), serializableLogRecord.permutationStrongName);
        event.response().end();
    }
}
