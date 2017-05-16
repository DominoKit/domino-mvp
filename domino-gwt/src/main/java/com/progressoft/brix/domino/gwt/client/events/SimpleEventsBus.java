package com.progressoft.brix.domino.gwt.client.events;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.Event;
import com.progressoft.brix.domino.api.client.events.EventsBus;
import com.progressoft.brix.domino.logger.client.CoreLogger;
import com.progressoft.brix.domino.logger.client.CoreLoggerFactory;

public class SimpleEventsBus implements EventsBus<Event<GwtEventProcessor>> {
    private static final CoreLogger LOGGER = CoreLoggerFactory.getLogger(SimpleEventsBus.class);

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
