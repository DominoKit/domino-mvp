package org.dominokit.domino.client.commons.request;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.async.AsyncRunner;
import org.dominokit.domino.api.client.events.ClientRequestEventFactory;
import org.dominokit.domino.api.client.request.PresenterCommand;
import org.dominokit.domino.rest.shared.request.RequestRouter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientRouter implements RequestRouter<PresenterCommand> {

    private static final Logger LOGGER = Logger.getLogger(ClientRouter.class.getName());

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
                LOGGER.log(Level.SEVERE, "Could not RunAsync request [" + presenterCommand + "]", error);
            }
        });
    }
}
