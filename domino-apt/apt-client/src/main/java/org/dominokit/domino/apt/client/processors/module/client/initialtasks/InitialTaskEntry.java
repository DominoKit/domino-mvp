package org.dominokit.domino.apt.client.processors.module.client.initialtasks;

import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;

public class InitialTaskEntry implements AbstractRegisterMethodWriter.ItemEntry {
    protected String initalTask;

    protected InitialTaskEntry(String initalTask) {
        this.initalTask = initalTask;
    }
}
