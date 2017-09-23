package com.progressoft.brix.domino.client.commons.request;

import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.client.request.RequestRouter;

public class ServerRouter implements RequestRouter<ClientServerRequest> {

    private final RequestAsyncSender requestAsyncRunner;

    public ServerRouter(RequestAsyncSender requestAsyncRunner) {
        this.requestAsyncRunner = requestAsyncRunner;
    }

    @Override
    public void routeRequest(final ClientServerRequest request) {
        requestAsyncRunner.send(request);
    }
}
