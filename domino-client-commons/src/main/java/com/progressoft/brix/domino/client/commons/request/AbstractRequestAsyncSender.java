package com.progressoft.brix.domino.client.commons.request;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.async.AsyncRunner;
import com.progressoft.brix.domino.api.client.events.ServerRequestEventFactory;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.client.request.ServerRequestCallBack;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRequestAsyncSender implements RequestAsyncSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestAsyncSender.class);
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
            ClientApp.make().getRequestRestSendersRepository().get(request.getClass().getCanonicalName())
                    .send(request.arguments(),
                            new ServerRequestCallBack() {
                                @Override
                                public void onFailure(Throwable throwable) {
                                    requestEventFactory.makeFailed(request, throwable).fire();
                                }

                                @Override
                                public void onSuccess(ServerResponse response) {
                                    requestEventFactory.makeSuccess(request, response).fire();
                                }
                            });
        }

        @Override
        public void onFailed(Throwable error) {
            LOGGER.error("Could not RunAsync request [" + request + "]", error);
        }
    }
}
