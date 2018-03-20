package org.dominokit.domino.api.shared.logging;

import java.io.Serializable;

public class SerializableLogRecord implements Serializable {
    public String level;
    public String message;
    public long millis;
    public SerializableThrowable thrown;
    public String loggerName;
    public String permutationStrongName;
}
