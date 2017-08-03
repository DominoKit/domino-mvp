package com.progressoft.brix.domino.api.shared.logging;

import java.io.Serializable;

public class SerializableStackTraceElement implements Serializable {
    public String className;
    public String fileName;
    public String methodName;
    public int lineNumber;
}
