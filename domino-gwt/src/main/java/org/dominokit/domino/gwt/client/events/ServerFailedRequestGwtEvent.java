package org.dominokit.domino.gwt.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.event.shared.Event;
import org.dominokit.domino.api.client.events.EventProcessor;

public abstract class ServerFailedRequestGwtEvent extends Event<EventProcessor> {

    protected static final GwtEvent.Type<EventProcessor> SERVER_FAILED_REQUEST_EVENT_TYPE = new GwtEvent.Type<>();

    @Override
    public Type<EventProcessor> getAssociatedType() {
        return SERVER_FAILED_REQUEST_EVENT_TYPE;
    }

}