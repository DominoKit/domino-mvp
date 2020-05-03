package org.dominokit.domino.api.shared.extension;

public interface GlobalDominoEventListener<E extends GlobalEvent> extends DominoEventListener<E> {
    E deserializeEvent(String serializedEvent);
}
