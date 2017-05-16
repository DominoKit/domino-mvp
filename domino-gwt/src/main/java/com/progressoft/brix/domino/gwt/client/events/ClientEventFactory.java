package com.progressoft.brix.domino.gwt.client.events;

import com.progressoft.brix.domino.api.client.events.ClientRequestEventFactory;
import com.progressoft.brix.domino.api.client.events.Event;
import com.progressoft.brix.domino.api.client.request.ClientRequest;

public class ClientEventFactory implements ClientRequestEventFactory {
    @Override
    public Event make(ClientRequest request) {
        return new ClientRequestEvent(request);
    }
}
