package org.dominokit.domino.apt.server.endpoints;

import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;
import org.dominokit.domino.apt.commons.ProcessorElement;

public class EndpointsEntry implements AbstractRegisterMethodWriter.ItemEntry {
    public ProcessorElement element;

    public EndpointsEntry(ProcessorElement element) {
        this.element = element;
    }
}
