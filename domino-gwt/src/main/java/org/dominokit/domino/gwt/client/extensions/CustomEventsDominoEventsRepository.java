package org.dominokit.domino.gwt.client.extensions;

import elemental2.dom.EventListener;
import elemental2.dom.*;
import jsinterop.base.Js;
import org.dominokit.domino.api.client.extension.DominoEventsListenersRepository;
import org.dominokit.domino.api.shared.extension.DominoEvent;
import org.dominokit.domino.api.shared.extension.DominoEventListener;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class CustomEventsDominoEventsRepository implements DominoEventsListenersRepository {

    private final Map<String, List<ListenerWrapper>> listeners = new HashMap<>();

    @Override
    public void addListener(Class<? extends DominoEvent> dominoEvent, DominoEventListener dominoEventListener) {
        initializeEventListeners(dominoEvent);
        ListenerWrapper wrapper = new ListenerWrapper(dominoEventListener);
        listeners.get(dominoEvent.getCanonicalName()).add(wrapper);
        DomGlobal.document.addEventListener(dominoEvent.getCanonicalName(), wrapper.dominoCustomEventListener);
        DomGlobal.console.info("Event listener added : "+dominoEvent.getCanonicalName());
    }

    @Override
    public Set<DominoEventListener> getEventListeners(Class<? extends DominoEvent> dominoEvent) {
        initializeEventListeners(dominoEvent);
        return listeners.get(dominoEvent.getCanonicalName()).stream().map(cw -> cw.dominoEventListener).collect(
                Collectors.toSet());
    }

    @Override
    public void fireEvent(Class<? extends DominoEvent> eventType, DominoEvent dominoEvent) {
        CustomEventInit customEventInit= CustomEventInit.create();
        customEventInit.setDetail(dominoEvent);
        CustomEvent customEvent = new CustomEvent(eventType.getCanonicalName(), customEventInit);
        DomGlobal.document.dispatchEvent(customEvent);
    }

    private List<ListenerWrapper> getEventListenersWrapper(Class<? extends DominoEvent> dominoEvent) {
        initializeEventListeners(dominoEvent);
        return listeners.get(dominoEvent.getCanonicalName());
    }

    @Override
    public void removeListener(Class<? extends DominoEvent> event, DominoEventListener listener) {
        List<ListenerWrapper> eventListeners = getEventListenersWrapper(event);
        int index = eventListeners.indexOf(new ListenerWrapper(listener));
        if (index >= 0){
            ListenerWrapper tobeRemoved = eventListeners.get(index);
            eventListeners.remove(tobeRemoved);
            DomGlobal.document.removeEventListener(event.getCanonicalName(), tobeRemoved.dominoCustomEventListener);
            DomGlobal.console.info("Event listener removed : "+event.getCanonicalName()+ " : "+ listener.getClass().getCanonicalName());
        }

        if (eventListeners.isEmpty()) {
            listeners.remove(event.getCanonicalName());
        }
    }

    private void initializeEventListeners(Class<? extends DominoEvent> extensionPoint) {
        if (isNull(listeners.get(extensionPoint.getCanonicalName())))
            listeners.put(extensionPoint.getCanonicalName(), new LinkedList<>());
    }

    public static class DominoCustomEventListener implements EventListener {

        private final DominoEventListener dominoEventListener;

        public DominoCustomEventListener(DominoEventListener dominoEventListener) {
            this.dominoEventListener = dominoEventListener;
        }

        @Override
        public void handleEvent(Event evt) {
            CustomEvent customEvent = Js.uncheckedCast(evt);
            dominoEventListener.onEventReceived(Js.uncheckedCast(customEvent.detail));
        }
    }

    private class ListenerWrapper {

        private final DominoEventListener dominoEventListener;
        private final DominoCustomEventListener dominoCustomEventListener;

        public ListenerWrapper(DominoEventListener dominoEventListener) {
            this.dominoEventListener = dominoEventListener;
            this.dominoCustomEventListener = new DominoCustomEventListener(dominoEventListener);
        }

        @Override
        public boolean equals(Object other) {
            if (isNull(other))
                return false;
            return dominoEventListener.getClass().getCanonicalName().equals(((ListenerWrapper) other).dominoEventListener.getClass().getCanonicalName());
        }

        @Override
        public int hashCode() {
            return dominoEventListener.getClass().getCanonicalName().hashCode();
        }
    }
}
