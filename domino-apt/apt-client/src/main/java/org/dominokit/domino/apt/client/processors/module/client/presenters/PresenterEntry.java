package org.dominokit.domino.apt.client.processors.module.client.presenters;

import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;

class PresenterEntry implements AbstractRegisterMethodWriter.ItemEntry{
    protected final String name;
    protected final boolean singleton;

    PresenterEntry(String name, boolean singleton) {
        this.name = name;
        this.singleton = singleton;
    }
}
