package com.progressoft.brix.domino.client.commons.request;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.async.AsyncRunner;
import com.progressoft.brix.domino.api.client.events.ServerRequestEventFactory;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;

public abstract class AbstractRequestAsyncSender implements RequestAsyncSender {

    //    private static final Logger LOGGER = LoggerFactory.getLogger(RequestAsyncSender.class);
    private final ServerRequestEventFactory requestEventFactory;

    public AbstractRequestAsyncSender(ServerRequestEventFactory requestEventFactory) {
        this.requestEventFactory = requestEventFactory;
    }

    @Override
    public final void send(final ClientServerRequest request) {
        ClientApp.make().getAsyncRunner().runAsync(new RequestAsyncTask(request));
    }

    private class RequestAsyncTask implements AsyncRunner.AsyncTask {
        private final ClientServerRequest request;

        private RequestAsyncTask(ClientServerRequest request) {
            this.request = request;
        }

        @Override
        public void onSuccess() {
            onBeforeSend(request);
            sendRequest(request, requestEventFactory);
        }

        @Override
        public void onFailed(Throwable error) {
//            LOGGER.error("Could not RunAsync request [" + request + "]", error);
        }
    }

    protected abstract void sendRequest(ClientServerRequest request, ServerRequestEventFactory requestEventFactory);
}
