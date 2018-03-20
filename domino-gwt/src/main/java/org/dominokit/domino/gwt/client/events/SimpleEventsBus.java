package org.dominokit.domino.gwt.client.events;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.Event;
import org.dominokit.domino.api.client.events.EventsBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleEventsBus implements EventsBus<Event<GwtEventProcessor>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEventsBus.class);

    private static final EventBus simpleGwtEventsBus = new SimpleEventBus();

    public SimpleEventsBus(GwtEventProcessor eventProcessor) {
        simpleGwtEventsBus.addHandler(ClientRequestGwtEvent.CLIENT_REQUEST_EVENT_TYPE, eventProcessor);
        simpleGwtEventsBus.addHandler(ServerSuccessRequestGwtEvent.SERVER_SUCCESS_REQUEST_EVENT_TYPE, eventProcessor);
        simpleGwtEventsBus.addHandler(ServerFailedRequestGwtEvent.SERVER_FAILED_REQUEST_EVENT_TYPE, eventProcessor);
    }

    @Override
    public void publishEvent(RequestEvent<Event<GwtEventProcessor>> event) {
        try {
            simpleGwtEventsBus.fireEvent(event.asEvent());
        } catch (Exception ex) {
            LOGGER.error("could not publish event", ex);
        }
    }
}
