package com.progressoft.brix.domino.gwt.client.events;

import com.progressoft.brix.domino.api.client.events.Event;

public class RequestEventProcessor implements GwtEventProcessor {

    @Override
    public void process(Event event) {
        event.process();
    }
}
