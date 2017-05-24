package com.progressoft.brix.domino.gwt.client.request;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.events.ServerRequestEventFactory;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.client.request.ServerRequestCallBack;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import com.progressoft.brix.domino.logger.client.CoreLogger;
import com.progressoft.brix.domino.logger.client.CoreLoggerFactory;
import org.fusesource.restygwt.client.Defaults;

public class AsyncRunner {
    private static final CoreLogger LOGGER = CoreLoggerFactory.getLogger(AsyncRunner.class);
    private final ServerRequestEventFactory requestEventFactory;

    public AsyncRunner(ServerRequestEventFactory requestEventFactory) {
        this.requestEventFactory = requestEventFactory;
        Defaults.setServiceRoot(GWT.getHostPageBaseURL() + "service");
        Defaults.setDispatcher(new DominoRequestDispatcher());
    }

    public final void run(final ClientServerRequest request) {
        com.google.gwt.core.client.GWT.runAsync(new GwtRunAsyncCallback(request));
    }

    private class GwtRunAsyncCallback implements RunAsyncCallback {
        private final ClientServerRequest request;

        public GwtRunAsyncCallback(ClientServerRequest request) {
            this.request = request;
        }

        @Override
        public void onFailure(Throwable t) {
            LOGGER.error("Could not RunAsync request [" + request + "]", t);
        }

        @Override
        public void onSuccess() {
            ClientApp.make().getRequestRestSendersRepository().get(request.getClass().getCanonicalName()).send(request.arguments(),
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
    }
}
