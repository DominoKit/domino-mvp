package org.dominokit.domino.api.shared.extension;

public abstract class GlobalEvent<C extends EventContext> implements DominoEvent<C> {

    public GlobalEvent() {
    }

    public GlobalEvent(String serializedEvent) {
    }

    public abstract String serialize();
}
