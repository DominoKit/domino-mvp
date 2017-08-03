package com.progressoft.brix.domino.api.server.logging;

import java.util.logging.LogRecord;

@FunctionalInterface
public interface RemoteLogger {
    void log(LogRecord logRecord, String permutationStrongName);
}
