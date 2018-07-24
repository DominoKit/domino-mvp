package org.dominokit.domino.client.commons.extensions;

import org.dominokit.domino.api.client.extension.DominoEventsListenersRepository;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.extension.DominoEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class InMemoryDominoEventsListenerRepository implements DominoEventsListenersRepository {

    private final Map<String, Set<ListenerWrapper>> listeners = new HashMap<>();

    @Override
    public void addListener(Class<? extends DominoEvent> dominoEvent, DominoEventListener dominoEventListener) {
        initializeEventListeners(dominoEvent);
        listeners.get(dominoEvent.getCanonicalName()).add(new ListenerWrapper(dominoEventListener));
    }

    @Override
    public Set<DominoEventListener> getEventListeners(Class<? extends DominoEvent> dominoEvent) {
        initializeEventListeners(dominoEvent);
        return listeners.get(dominoEvent.getCanonicalName()).stream().map(cw -> cw.dominoEventListener).collect(
                Collectors.toSet());
    }

    private void initializeEventListeners(Class<? extends DominoEvent> extensionPoint) {
        if (isNull(listeners.get(extensionPoint.getCanonicalName())))
            listeners.put(extensionPoint.getCanonicalName(), new HashSet<>());
    }

    private class ListenerWrapper {

        private final DominoEventListener dominoEventListener;

        public ListenerWrapper(DominoEventListener dominoEventListener) {
            this.dominoEventListener = dominoEventListener;
        }

        @Override
        public boolean equals(Object other) {
            if(isNull(other))
                return false;
            return dominoEventListener.getClass().getCanonicalName().equals(((ListenerWrapper) other).dominoEventListener.getClass().getCanonicalName());
        }

        @Override
        public int hashCode() {
            return dominoEventListener.getClass().getCanonicalName().hashCode();
        }
    }
}
