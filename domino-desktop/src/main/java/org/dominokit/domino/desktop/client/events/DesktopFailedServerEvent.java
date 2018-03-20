package org.dominokit.domino.desktop.client.events;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.events.Event;
import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.client.request.Request;
import org.dominokit.domino.api.shared.request.FailedResponseBean;

public class DesktopFailedServerEvent implements Event {
    private final ServerRequest request;
    private final Throwable error;

    public DesktopFailedServerEvent(ServerRequest request, Throwable error) {
        this.request = request;
        this.error = error;
    }

    @Override
    public void fire() {
        ClientApp.make().getAsyncRunner().runAsync(this::process);
    }

    @Override
    public void process() {
        request.applyState(new Request.ServerResponseReceivedStateContext(makeFailedContext()));
    }

    private Request.ServerFailedRequestStateContext makeFailedContext() {
        return new Request.ServerFailedRequestStateContext(new FailedResponseBean(error));
    }
}
