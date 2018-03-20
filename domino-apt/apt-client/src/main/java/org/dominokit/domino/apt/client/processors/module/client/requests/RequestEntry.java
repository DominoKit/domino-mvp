package org.dominokit.domino.apt.client.processors.module.client.requests;

import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;

class RequestEntry implements AbstractRegisterMethodWriter.ItemEntry {
    protected final String request;
    protected final String presenter;

    public RequestEntry(String request, String presenter) {
        this.request = request;
        this.presenter = presenter;
    }
}
