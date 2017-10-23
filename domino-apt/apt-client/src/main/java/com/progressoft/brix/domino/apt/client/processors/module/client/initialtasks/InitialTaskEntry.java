package com.progressoft.brix.domino.apt.client.processors.module.client.initialtasks;

import com.progressoft.brix.domino.apt.client.processors.module.client.AbstractRegisterMethodWriter;

public class InitialTaskEntry implements AbstractRegisterMethodWriter.ItemEntry {
    protected String initalTask;

    protected InitialTaskEntry(String initalTask) {
        this.initalTask = initalTask;
    }
}
