package org.dominokit.domino.desktop.client.events;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.events.Event;
import org.dominokit.domino.api.shared.request.Request;
import org.dominokit.domino.api.shared.request.ServerRequest;

public class DesktopSuccessServerEvent<T> implements Event {
    private final ServerRequest request;
    private final T responseBean;

    public DesktopSuccessServerEvent(ServerRequest request, T responseBean) {
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
