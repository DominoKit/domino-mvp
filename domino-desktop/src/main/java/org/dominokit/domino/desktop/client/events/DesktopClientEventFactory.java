package org.dominokit.domino.desktop.client.events;

import org.dominokit.domino.api.client.events.ClientRequestEventFactory;
import org.dominokit.domino.api.client.events.Event;
import org.dominokit.domino.api.client.request.PresenterCommand;

public class DesktopClientEventFactory implements ClientRequestEventFactory {
    @Override
    public Event make(PresenterCommand request) {
        return new DesktopClientEvent(request);
    }
}
