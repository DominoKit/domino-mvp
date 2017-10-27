package com.progressoft.brix.domino.desktop.client.events;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.events.Event;
import com.progressoft.brix.domino.api.client.request.ServerRequest;
import com.progressoft.brix.domino.api.client.request.Request;
import com.progressoft.brix.domino.api.shared.request.FailedResponseBean;

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
