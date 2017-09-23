package com.progressoft.brix.domino.desktop.client.events;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.events.Event;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.client.request.Request;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;

public class DesktopSuccessServerEvent implements Event {
    private final ClientServerRequest request;
    private final ServerResponse serverResponse;

    public DesktopSuccessServerEvent(ClientServerRequest request, ServerResponse serverResponse) {
        this.request = request;
        this.serverResponse = serverResponse;
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
        return new Request.ServerSuccessRequestStateContext(serverResponse);
    }
}
