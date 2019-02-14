package org.dominokit.domino.apt.server.handlers;

import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;
import org.dominokit.domino.apt.commons.ProcessorElement;

public class ResourceEntry implements AbstractRegisterMethodWriter.ItemEntry {
    protected ProcessorElement resource;

    public ResourceEntry(ProcessorElement resource) {
        this.resource = resource;
    }
}
