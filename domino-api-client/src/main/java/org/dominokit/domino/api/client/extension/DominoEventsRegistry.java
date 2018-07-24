package org.dominokit.domino.api.client.extension;

import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.extension.DominoEvent;

@FunctionalInterface
public interface DominoEventsRegistry {
    void addListener(Class<? extends DominoEvent> event, DominoEventListener dominoEventListener);
}
