package com.progressoft.brix.domino.gwt.client.events;

import com.progressoft.brix.domino.api.client.events.ClientRequestEventFactory;
import com.progressoft.brix.domino.api.client.events.Event;
import com.progressoft.brix.domino.api.client.request.PresenterCommand;

public class ClientEventFactory implements ClientRequestEventFactory {
    @SuppressWarnings("rawtypes")
    @Override
    public Event make(PresenterCommand request) {
        return new ClientRequestEvent(request);
    }
}
