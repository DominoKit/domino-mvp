package org.dominokit.domino.desktop.client.events;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.events.Event;
import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.client.request.Request;
import org.dominokit.domino.api.shared.request.ResponseBean;

public class DesktopSuccessServerEvent implements Event {
    private final ServerRequest request;
    private final ResponseBean responseBean;

    public DesktopSuccessServerEvent(ServerRequest request, ResponseBean responseBean) {
        this.request = request;
        this.responseBean = responseBean;
    }

    @Override
    public void fire() {
        ClientApp.make().getAsyncRunner().runAsync(this::process);
    }

    @Override
    public void process() {
        request.applyState(new Request.ServerResponseReceivedStateContext(makeSuccessContext()));
    }

    private Request.ServerSuccessRequestStateContext makeSuccessContext() {
        return new Request.ServerSuccessRequestStateContext(responseBean);
    }
}
