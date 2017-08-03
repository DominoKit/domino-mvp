package com.progressoft.brix.domino.api.server.logging;

import com.progressoft.brix.domino.api.shared.logging.SerializableLogRecord;
import com.progressoft.brix.domino.api.shared.logging.SerializableStackTraceElement;
import com.progressoft.brix.domino.api.shared.logging.SerializableThrowable;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static java.util.Objects.isNull;

public class SerializableLogRecordMapper {

    private SerializableLogRecordMapper() {
    }

    static LogRecord asLogRecord(SerializableLogRecord serializableLogRecord) {
        LogRecord logRecord = new LogRecord(Level.parse(serializableLogRecord.level), serializableLogRecord.message);
        logRecord.setLoggerName(serializableLogRecord.loggerName);
        logRecord.setMillis(serializableLogRecord.millis);
        logRecord.setThrown(JsonLogRecordThrowable.fromJsonString(serializableLogRecord.thrown));
        return logRecord;
    }

    private static class JsonLogRecordThrowable extends Throwable {
        private final String type;

        private JsonLogRecordThrowable(SerializableThrowable serializableThrowable) {
            super(serializableThrowable.message, fromJsonString(serializableThrowable.cause));
            this.type = serializableThrowable.type;
            this.setStackTrace(fromSerializableStackTrace(serializableThrowable.stackTrace));
        }

        private StackTraceElement[] fromSerializableStackTrace(SerializableStackTraceElement[] serializableStackTrace) {
            if (isNull(serializableStackTrace))
                serializableStackTrace = new SerializableStackTraceElement[0];
            return Arrays.stream(serializableStackTrace).map(this::asStackTrace).toArray(StackTraceElement[]::new);
        }

        private StackTraceElement asStackTrace(SerializableStackTraceElement e) {
            return new StackTraceElement(e.className, e.methodName, e.fileName, e.lineNumber);
        }

        public String toString() {
            return getMessage() != null ? type + ": " + getMessage() : type;
        }

        private static Throwable fromJsonString(SerializableThrowable serializableThrowable) {
            return serializableThrowable == null ? null : new JsonLogRecordThrowable(serializableThrowable);
        }
    }
}
