package com.progressoft.brix.domino.client.commons.request;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.async.AsyncRunner;
import com.progressoft.brix.domino.api.client.events.ClientRequestEventFactory;
import com.progressoft.brix.domino.api.client.request.PresenterCommand;
import com.progressoft.brix.domino.api.client.request.RequestRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientRouter implements RequestRouter<PresenterCommand> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRouter.class);

    private final ClientRequestEventFactory requestEventFactory;

    public ClientRouter(ClientRequestEventFactory requestEventFactory) {
        this.requestEventFactory = requestEventFactory;
    }

    @Override
    public void routeRequest(final PresenterCommand presenterCommand) {

        ClientApp.make().getAsyncRunner().runAsync(new AsyncRunner.AsyncTask() {
            @Override
            public void onSuccess() {
                requestEventFactory.make(presenterCommand).fire();
            }

            @Override
            public void onFailed(Throwable error) {
                LOGGER.error("Could not RunAsync request [" + presenterCommand + "]", error);
            }
        });
    }
}
