package org.dominokit.domino.api.server.logging;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class DefaultRemoteLogger implements RemoteLogger {

    private static final Logger LOGGER = Logger.getLogger(DefaultRemoteLogger.class.getName());

    @Override
    public void log(LogRecord logRecord, String permutationStrongName) {
        LOGGER.log(logRecord);
    }
}
