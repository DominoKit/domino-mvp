package org.dominokit.domino.gwt.client.events;

import org.dominokit.domino.api.client.events.Event;

public class RequestEventProcessor implements GwtEventProcessor {

    @Override
    public void process(Event event) {
        event.process();
    }
}
