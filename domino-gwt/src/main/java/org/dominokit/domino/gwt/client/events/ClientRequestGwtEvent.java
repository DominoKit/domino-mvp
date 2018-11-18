package org.dominokit.domino.gwt.client.events;

import org.dominokit.domino.api.client.events.EventProcessor;
import org.gwtproject.event.shared.Event;

public abstract class ClientRequestGwtEvent extends Event<EventProcessor> {

    protected static final Event.Type<EventProcessor> CLIENT_REQUEST_EVENT_TYPE = new Event.Type<>();

    @Override
    public Type<EventProcessor> getAssociatedType() {
        return CLIENT_REQUEST_EVENT_TYPE;
    }

}