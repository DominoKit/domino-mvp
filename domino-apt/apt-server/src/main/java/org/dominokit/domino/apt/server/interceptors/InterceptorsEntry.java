package org.dominokit.domino.apt.server.interceptors;

import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;
import org.dominokit.domino.apt.commons.ProcessorElement;

public class InterceptorsEntry implements AbstractRegisterMethodWriter.ItemEntry {
    public ProcessorElement element;

    public InterceptorsEntry(ProcessorElement element) {
        this.element = element;
    }
}
