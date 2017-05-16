package com.progressoft.brix.domino.gwt.client.events;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.events.Event;
import com.progressoft.brix.domino.api.client.events.EventProcessor;
import com.progressoft.brix.domino.api.client.events.EventsBus;
import com.progressoft.brix.domino.api.client.request.ClientRequest;
import com.progressoft.brix.domino.api.client.request.Request;

public class ClientRequestEvent extends ClientRequestGwtEvent implements Event {

    protected final ClientRequest request;
    private final ClientApp clientApp = ClientApp.make();

    public ClientRequestEvent(ClientRequest request) {
        this.request = request;
    }

    @Override
    public void fire() {
        clientApp.getEventsBus().publishEvent(new GWTRequestEvent(this));
    }

    @Override
    public void process() {
        request.applyState(new Request.DefaultRequestStateContext());
    }

    @Override
    protected void dispatch(EventProcessor eventProcessor) {
        eventProcessor.process(this);
    }

    private class GWTRequestEvent implements EventsBus.RequestEvent<ClientRequestGwtEvent> {

        private final ClientRequestGwtEvent event;

        public GWTRequestEvent(ClientRequestGwtEvent event) {
            this.event = event;
        }

        @Override
        public ClientRequestGwtEvent asEvent() {
            return event;
        }
    }
}
