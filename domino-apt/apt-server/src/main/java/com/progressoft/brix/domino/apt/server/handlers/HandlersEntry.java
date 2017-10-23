package com.progressoft.brix.domino.apt.server.handlers;

import com.progressoft.brix.domino.apt.commons.AbstractRegisterMethodWriter;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;

public class HandlersEntry implements AbstractRegisterMethodWriter.ItemEntry {
    protected ProcessorElement handler;

    public HandlersEntry(ProcessorElement handler) {
        this.handler = handler;
    }
}
