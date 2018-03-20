package org.dominokit.domino.apt.commons;

import java.io.IOException;

public abstract class JavaSourceWriter {

    protected final ProcessorElement processorElement;

    public JavaSourceWriter(ProcessorElement processorElement) {
        this.processorElement = processorElement;
    }

    public abstract String write() throws IOException;
}
