package org.dominokit.domino.gwt.client.events;

import org.dominokit.domino.api.client.events.ClientRequestEventFactory;
import org.dominokit.domino.api.client.request.PresenterCommand;
import org.dominokit.domino.rest.shared.Event;

public class ClientEventFactory implements ClientRequestEventFactory {
    @SuppressWarnings("rawtypes")
    @Override
    public Event make(PresenterCommand request) {
        return new ClientRequestEvent(request);
    }
}
