package com.progressoft.brix.domino.apt.server.endpoints;

import com.progressoft.brix.domino.apt.commons.AbstractRegisterMethodWriter;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;

public class EndpointsEntry implements AbstractRegisterMethodWriter.ItemEntry {
    public ProcessorElement element;

    public EndpointsEntry(ProcessorElement element) {
        this.element = element;
    }
}
