package com.progressoft.brix.domino.client.commons.request;

import com.progressoft.brix.domino.api.client.request.ServerRequest;
import com.progressoft.brix.domino.api.client.request.RequestRouter;

public class ServerRouter implements RequestRouter<ServerRequest> {

    private final RequestAsyncSender requestAsyncRunner;

    public ServerRouter(RequestAsyncSender requestAsyncRunner) {
        this.requestAsyncRunner = requestAsyncRunner;
    }

    @Override
    public void routeRequest(final ServerRequest request) {
        requestAsyncRunner.send(request);
    }
}
