package com.progressoft.brix.domino.desktop.client.events;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.events.Event;
import com.progressoft.brix.domino.api.client.events.EventsBus;

public class DesktopEventBus implements EventsBus<Event> {

    @Override
    public void publishEvent(RequestEvent<Event> event) {
        ClientApp.make().getAsyncRunner().runAsync(() -> event.asEvent().process());
    }
}
