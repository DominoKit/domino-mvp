package org.dominokit.domino.api.shared.extension;

@FunctionalInterface
public interface DominoEvent<C extends EventContext> {
    C context();
}
