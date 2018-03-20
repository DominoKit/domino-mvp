package org.dominokit.domino.remote.logging;

import com.google.auto.service.AutoService;
import com.google.gwt.core.server.StackTraceDeobfuscator;
import org.dominokit.domino.api.server.logging.RemoteLogger;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@AutoService(RemoteLogger.class)
public class DeobfuscatorRemoteLogger implements RemoteLogger {

    private static final Logger LOGGER = Logger.getLogger(DeobfuscatorRemoteLogger.class.getName());
    private StackTraceDeobfuscator deobfuscator = new DominoStackTraceDeobfuscator();

    @Override
    public void log(LogRecord logRecord, String permutationStrongName) {
        if (isError(logRecord))
            deobfuscator.deobfuscateStackTrace(logRecord.getThrown(), permutationStrongName);
        LOGGER.log(logRecord);
    }

    private boolean isError(LogRecord logRecord) {
        return logRecord.getLevel().equals(Level.SEVERE);
    }
}
