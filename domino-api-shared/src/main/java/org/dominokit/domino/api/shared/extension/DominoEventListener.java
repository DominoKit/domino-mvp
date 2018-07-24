package org.dominokit.domino.api.shared.extension;

@FunctionalInterface
public interface DominoEventListener<E extends DominoEvent> {
    void listen(E dominoEvent);
}
