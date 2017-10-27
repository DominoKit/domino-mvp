package com.progressoft.brix.domino.desktop.client.events;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.events.Event;
import com.progressoft.brix.domino.api.client.request.ServerRequest;
import com.progressoft.brix.domino.api.client.request.Request;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

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
