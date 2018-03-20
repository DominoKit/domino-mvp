package org.dominokit.domino.gwt.client.events;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.events.Event;
import org.dominokit.domino.api.client.events.EventProcessor;
import org.dominokit.domino.api.client.events.EventsBus;
import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.client.request.Request;
import org.dominokit.domino.api.shared.request.ResponseBean;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ServerSuccessRequestEvent extends ServerSuccessRequestGwtEvent implements Event {

    protected final ServerRequest request;
    private final ResponseBean responseBean;
    private final ClientApp clientApp = ClientApp.make();

    public ServerSuccessRequestEvent(ServerRequest request, ResponseBean responseBean) {
        this.request = request;
        this.responseBean = responseBean;
    }

    @Override
    public void fire() {
        clientApp.getEventsBus().publishEvent(new GWTRequestEvent(this));
    }

    @Override
    public void process() {
        request.applyState(new Request.ServerResponseReceivedStateContext(makeSuccessContext()));
    }

    private Request.ServerSuccessRequestStateContext makeSuccessContext() {
        return new Request.ServerSuccessRequestStateContext(responseBean);
    }

    @Override
    protected void dispatch(EventProcessor eventProcessor) {
        eventProcessor.process(this);
    }

    private class GWTRequestEvent implements EventsBus.RequestEvent<ServerSuccessRequestGwtEvent> {

        private final ServerSuccessRequestGwtEvent event;

        public GWTRequestEvent(ServerSuccessRequestGwtEvent event) {
            this.event = event;
        }

        @Override
        public ServerSuccessRequestGwtEvent asEvent() {
            return event;
        }
    }
}
