package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.extension.DominoEvent;
import org.dominokit.domino.client.commons.extensions.InMemoryDominoEventsListenerRepository;

import java.util.HashMap;
import java.util.Map;

public class TestInMemoryEventsListenersRepository extends InMemoryDominoEventsListenerRepository {

    protected final Map<String, DominoEventListener> testListeners = new HashMap<>();

    @Override
    public void addListener(Class<? extends DominoEvent> dominoEvent, DominoEventListener dominoEventListener) {
        super.addListener(dominoEvent, dominoEventListener);
        testListeners.put(dominoEventListener.getClass().getCanonicalName(), dominoEventListener);
    }

    public <L extends DominoEventListener> L getListener(Class<L> listenerClass) {
        return (L) testListeners.get(listenerClass.getCanonicalName());
    }
}
