package org.dominokit.domino.api.shared.logging;

import java.io.Serializable;

public class SerializableThrowable implements Serializable {
    public String type;
    public String message;
    public SerializableThrowable cause;
    public SerializableStackTraceElement[] stackTrace;
}
