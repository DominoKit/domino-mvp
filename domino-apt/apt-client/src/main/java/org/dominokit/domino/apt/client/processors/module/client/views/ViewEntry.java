package org.dominokit.domino.apt.client.processors.module.client.views;

import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;

public class ViewEntry implements AbstractRegisterMethodWriter.ItemEntry {
    protected final String view;
    protected final String presenter;
    protected final boolean singleton;

    public ViewEntry(String view, String presenter, boolean singleton) {
        this.view = view;
        this.presenter = presenter;
        this.singleton = singleton;
    }
}
