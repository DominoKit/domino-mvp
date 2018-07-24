package org.dominokit.domino.api.client.extension;

import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.extension.DominoEvent;

import java.util.Set;

public interface DominoEventsListenersRepository {
    void addListener(Class<? extends DominoEvent> dominoEvent, DominoEventListener dominoEventListener);
    Set<DominoEventListener> getEventListeners(Class<? extends DominoEvent> dominoEvent);
}
