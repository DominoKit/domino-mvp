package com.progressoft.brix.domino.gwt.client.request;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.progressoft.brix.domino.api.client.events.ClientRequestEventFactory;
import com.progressoft.brix.domino.api.client.request.ClientRequest;
import com.progressoft.brix.domino.api.client.request.RequestRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientRouter implements RequestRouter<ClientRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRouter.class);

    private final ClientRequestEventFactory requestEventFactory;

    public ClientRouter(ClientRequestEventFactory requestEventFactory) {
        this.requestEventFactory = requestEventFactory;
    }

    @Override
    public void routeRequest(final ClientRequest request) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable t) {
                LOGGER.error("Could not RunAsync request [" + request + "]", t);
            }

            @Override
            public void onSuccess() {
                requestEventFactory.make(request).fire();
            }
        });
    }
}
