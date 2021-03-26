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

import static java.util.Optional.ofNullable;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.dominokit.domino.api.shared.logging.SerializableLogRecord;
import org.dominokit.domino.api.shared.logging.SerializableStackTraceElement;
import org.dominokit.domino.api.shared.logging.SerializableThrowable;

public class SerializableLogRecordMapper {

  private SerializableLogRecordMapper() {}

  public static LogRecord asLogRecord(SerializableLogRecord serializableLogRecord) {
    LogRecord logRecord =
        new LogRecord(Level.parse(serializableLogRecord.level), serializableLogRecord.message);
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

    private StackTraceElement[] fromSerializableStackTrace(
        SerializableStackTraceElement[] serializableStackTrace) {
      return Arrays.stream(
              ofNullable(serializableStackTrace).orElse(new SerializableStackTraceElement[0]))
          .map(this::asStackTrace)
          .toArray(StackTraceElement[]::new);
    }

    private StackTraceElement asStackTrace(SerializableStackTraceElement e) {
      return new StackTraceElement(e.className, e.methodName, e.fileName, e.lineNumber);
    }

    public String toString() {
      return getMessage() != null ? type + ": " + getMessage() : type;
    }

    private static Throwable fromJsonString(SerializableThrowable serializableThrowable) {
      return serializableThrowable == null
          ? null
          : new JsonLogRecordThrowable(serializableThrowable);
    }
  }
}
