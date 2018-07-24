package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.shared.extension.DominoEventListener;

@FunctionalInterface
public interface ListenerHandler<L extends DominoEventListener> {
    void handle(L listener);
}
