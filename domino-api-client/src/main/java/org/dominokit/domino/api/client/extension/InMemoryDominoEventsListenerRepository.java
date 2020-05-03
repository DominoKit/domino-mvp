package org.dominokit.domino.api.client.extension;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.shared.extension.DominoEvent;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.extension.GlobalDominoEventListener;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class InMemoryDominoEventsListenerRepository implements DominoEventsListenersRepository {

    private final Map<String, List<ListenerWrapper>> listeners = new HashMap<>();

    @Override
    public void addListener(Class<? extends DominoEvent> dominoEvent, DominoEventListener dominoEventListener) {
        initializeEventListeners(dominoEvent);
        listeners.get(dominoEvent.getCanonicalName()).add(new ListenerWrapper(dominoEventListener));
    }

    @Override
    public void addGlobalListener(Class<? extends DominoEvent> dominoEvent, GlobalDominoEventListener dominoEventListener) {
        addListener(dominoEvent, dominoEventListener);
    }

    @Override
    public Set<DominoEventListener> getEventListeners(Class<? extends DominoEvent> dominoEvent) {
        initializeEventListeners(dominoEvent);
        return listeners.get(dominoEvent.getCanonicalName()).stream().map(cw -> cw.dominoEventListener).collect(
                Collectors.toSet());
    }

    @Override
    public void fireEvent(Class<? extends DominoEvent> eventType, DominoEvent dominoEvent) {
        getEventListeners(eventType)
                .forEach(listener ->
                        ClientApp.make().getAsyncRunner().runAsync(() -> listener.onEventReceived(dominoEvent)));
    }

    private List<ListenerWrapper> getEventListenersWrapper(Class<? extends DominoEvent> dominoEvent) {
        initializeEventListeners(dominoEvent);
        return listeners.get(dominoEvent.getCanonicalName());
    }

    @Override
    public void removeListener(Class<? extends DominoEvent> event, DominoEventListener listener) {
        List<ListenerWrapper> eventListeners = getEventListenersWrapper(event);
        eventListeners.remove(new ListenerWrapper(listener));

        if(eventListeners.isEmpty()){
            listeners.remove(event.getCanonicalName());
        }
    }

    @Override
    public void removeGlobalListener(Class<? extends DominoEvent> event, GlobalDominoEventListener listener) {
        removeListener(event, listener);
    }

    private void initializeEventListeners(Class<? extends DominoEvent> extensionPoint) {
        if (isNull(listeners.get(extensionPoint.getCanonicalName())))
            listeners.put(extensionPoint.getCanonicalName(), new LinkedList<>());
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
