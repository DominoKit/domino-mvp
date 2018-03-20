package org.dominokit.domino.apt.server.interceptors.global;

import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;
import org.dominokit.domino.apt.commons.ProcessorElement;

public class GlobalInterceptorsEntry implements AbstractRegisterMethodWriter.ItemEntry {
    public ProcessorElement element;

    public GlobalInterceptorsEntry(ProcessorElement element) {
        this.element = element;
    }
}
