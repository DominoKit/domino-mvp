package com.progressoft.brix.domino.gwt.client.request;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.async.AsyncRunner.AsyncTask;
import com.progressoft.brix.domino.api.client.events.ServerRequestEventFactory;
import com.progressoft.brix.domino.api.client.mvp.presenter.Presentable;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.client.request.ServerRequestCallBack;
import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import org.fusesource.restygwt.client.Defaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RequestAsyncRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestAsyncRunner.class);
    private final ServerRequestEventFactory requestEventFactory;
    private final DominoRequestDispatcher dispatcher = new DominoRequestDispatcher();

    RequestAsyncRunner(ServerRequestEventFactory requestEventFactory) {
        this.requestEventFactory = requestEventFactory;
        Defaults.setDispatcher(dispatcher);
    }

    public final void run(final ClientServerRequest request) {
        ClientApp.make().getAsyncRunner().runAsync(new RequestAsyncTask(request));
    }

    private class RequestAsyncTask<P extends Presentable, R extends ServerRequest, S extends ServerResponse> implements AsyncTask {
        private final ClientServerRequest<P, R, S> request;

        private RequestAsyncTask(ClientServerRequest<P, R, S> request) {
            this.request = request;
        }

        @Override
        public void onSuccess() {
            dispatcher.withHeaders(request.headers());
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
