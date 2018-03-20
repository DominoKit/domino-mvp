package org.dominokit.domino.apt.server.handlers;

import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;
import org.dominokit.domino.apt.commons.ProcessorElement;

public class HandlersEntry implements AbstractRegisterMethodWriter.ItemEntry {
    protected ProcessorElement handler;

    public HandlersEntry(ProcessorElement handler) {
        this.handler = handler;
    }
}
