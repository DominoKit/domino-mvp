package org.dominokit.domino.gwt.client.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.event.shared.Event;
import org.dominokit.domino.api.client.events.EventProcessor;

public abstract class ServerSuccessRequestGwtEvent extends Event<EventProcessor> {

    static final GwtEvent.Type<EventProcessor> SERVER_SUCCESS_REQUEST_EVENT_TYPE = new GwtEvent.Type<>();

    @Override
    public Type<EventProcessor> getAssociatedType() {
        return SERVER_SUCCESS_REQUEST_EVENT_TYPE;
    }

}