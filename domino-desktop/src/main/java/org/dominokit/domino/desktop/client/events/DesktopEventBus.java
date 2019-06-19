package org.dominokit.domino.desktop.client.events;


import org.dominokit.domino.rest.shared.Event;
import org.dominokit.domino.rest.shared.EventsBus;

public class DesktopEventBus implements EventsBus<Event> {

    @Override
    public void publishEvent(RequestEvent<Event> event) {
        event.asEvent().process();
    }
}
