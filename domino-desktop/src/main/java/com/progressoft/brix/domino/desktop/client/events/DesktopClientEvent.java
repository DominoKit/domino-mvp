package com.progressoft.brix.domino.desktop.client.events;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.events.Event;
import com.progressoft.brix.domino.api.client.request.ClientRequest;
import com.progressoft.brix.domino.api.client.request.Request;

public class DesktopClientEvent implements Event {

    private final ClientRequest request;

    public DesktopClientEvent(ClientRequest request) {
        this.request = request;
    }

    @Override
    public void fire() {
        ClientApp.make().getEventsBus().publishEvent(() -> this);
    }

    @Override
    public void process() {
        request.applyState(new Request.DefaultRequestStateContext());
    }
}
