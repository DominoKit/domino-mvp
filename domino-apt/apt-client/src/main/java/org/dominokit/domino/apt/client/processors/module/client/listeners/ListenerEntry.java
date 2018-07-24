package org.dominokit.domino.apt.client.processors.module.client.listeners;

import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;

class ListenerEntry implements AbstractRegisterMethodWriter.ItemEntry {
    protected final String listener;
    protected final String dominoEvent;

    protected ListenerEntry(String listener, String dominoEvent) {
        this.listener = listener;
        this.dominoEvent = dominoEvent;
    }
}
