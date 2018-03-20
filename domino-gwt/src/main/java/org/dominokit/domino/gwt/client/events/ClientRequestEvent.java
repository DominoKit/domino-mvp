package org.dominokit.domino.gwt.client.events;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.events.Event;
import org.dominokit.domino.api.client.events.EventProcessor;
import org.dominokit.domino.api.client.events.EventsBus;
import org.dominokit.domino.api.client.request.PresenterCommand;
import org.dominokit.domino.api.client.request.Request;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ClientRequestEvent extends ClientRequestGwtEvent implements Event {

    protected final PresenterCommand request;
    private final ClientApp clientApp = ClientApp.make();

    ClientRequestEvent(PresenterCommand request) {
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

        GWTRequestEvent(ClientRequestGwtEvent event) {
            this.event = event;
        }

        @Override
        public ClientRequestGwtEvent asEvent() {
            return event;
        }
    }
}
