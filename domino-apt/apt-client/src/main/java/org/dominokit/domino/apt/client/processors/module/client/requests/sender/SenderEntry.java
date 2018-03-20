package org.dominokit.domino.apt.client.processors.module.client.requests.sender;

import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;

class SenderEntry implements AbstractRegisterMethodWriter.ItemEntry {
    protected final String sender;
    protected final String request;

    public SenderEntry(String sender, String request) {
        this.sender = sender;
        this.request = request;
    }
}
