package com.progressoft.brix.domino.gwt.client.request;

import com.progressoft.brix.domino.api.client.async.AsyncRunner.AsyncTask;
import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.events.ServerRequestEventFactory;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.client.request.ServerRequestCallBack;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import org.fusesource.restygwt.client.Defaults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.gwt.core.client.GWT.*;

public class RequestAsyncRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestAsyncRunner.class);
    private final ServerRequestEventFactory requestEventFactory;

    public RequestAsyncRunner(ServerRequestEventFactory requestEventFactory) {
        this.requestEventFactory = requestEventFactory;
        Defaults.setServiceRoot(getHostPageBaseURL() + "service");
        Defaults.setDispatcher(new DominoRequestDispatcher());
    }

    public final void run(final ClientServerRequest request) {
        ClientApp.make().asyncRunner().runAsync(new RequestAsyncTask(request));
    }

    private class RequestAsyncTask implements AsyncTask {
        private final ClientServerRequest request;

        public RequestAsyncTask(ClientServerRequest request) {
            this.request = request;
        }

        @Override
        public void onSuccess() {
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
