package org.dominokit.domino.desktop.client.events;

import org.dominokit.domino.api.client.events.Event;
import org.dominokit.domino.api.client.events.EventsBus;

public class DesktopEventBus implements EventsBus<Event> {

    @Override
    public void publishEvent(RequestEvent<Event> event) {
        event.asEvent().process();
    }
}
