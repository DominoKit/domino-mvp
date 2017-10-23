package com.progressoft.brix.domino.apt.client.processors.module.client.views;

import com.progressoft.brix.domino.apt.commons.AbstractRegisterMethodWriter;

public class ViewEntry implements AbstractRegisterMethodWriter.ItemEntry {
    protected final String view;
    protected final String presenter;

    public ViewEntry(String view, String presenter) {
        this.view = view;
        this.presenter = presenter;
    }
}
