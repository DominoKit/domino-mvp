package com.progressoft.brix.domino.gwt.client.request;


import com.progressoft.brix.domino.api.client.events.ServerRequestEventFactory;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.client.request.RequestRouter;

public class ServerRouter implements RequestRouter<ClientServerRequest> {

    private final RequestAsyncRunner requestAsyncRunner;

    public ServerRouter(ServerRequestEventFactory requestEventFactory) {
        requestAsyncRunner = new RequestAsyncRunner(requestEventFactory);
    }

    @Override
    public void routeRequest(final ClientServerRequest request) {
        requestAsyncRunner.run(request);
    }
}
