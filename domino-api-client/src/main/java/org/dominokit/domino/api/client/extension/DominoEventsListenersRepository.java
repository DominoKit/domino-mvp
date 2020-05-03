package org.dominokit.domino.api.client.extension;

import org.dominokit.domino.api.shared.extension.DominoEvent;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.extension.GlobalDominoEventListener;

import java.util.Set;

public interface DominoEventsListenersRepository {
    void addListener(Class<? extends DominoEvent> dominoEvent, DominoEventListener dominoEventListener);
    void addGlobalListener(Class<? extends DominoEvent> dominoEvent, GlobalDominoEventListener dominoEventListener);
    Set<DominoEventListener> getEventListeners(Class<? extends DominoEvent> dominoEvent);

    void removeListener(Class<? extends DominoEvent> event, DominoEventListener listener);
    void removeGlobalListener(Class<? extends DominoEvent> event, GlobalDominoEventListener listener);

    void fireEvent(Class<? extends DominoEvent> eventType, DominoEvent dominoEvent);
}
