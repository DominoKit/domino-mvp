package com.progressoft.brix.domino.gwt.client.request;


import com.progressoft.brix.domino.api.client.events.ServerRequestEventFactory;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.client.request.RequestRouter;

public class ServerRouter implements RequestRouter<ClientServerRequest> {

    private final AsyncRunner asyncRunner;

    public ServerRouter(ServerRequestEventFactory requestEventFactory) {
        asyncRunner = new AsyncRunner(requestEventFactory);
    }

    @Override
    public void routeRequest(final ClientServerRequest request) {
        asyncRunner.run(request);
    }
}
