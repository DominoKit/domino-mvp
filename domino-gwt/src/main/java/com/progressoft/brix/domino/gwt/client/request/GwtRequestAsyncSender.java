package com.progressoft.brix.domino.gwt.client.request;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.events.ServerRequestEventFactory;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.client.request.ServerRequestCallBack;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import com.progressoft.brix.domino.client.commons.request.AbstractRequestAsyncSender;
import org.fusesource.restygwt.client.Defaults;

public class GwtRequestAsyncSender extends AbstractRequestAsyncSender {

    private final DominoRequestDispatcher dispatcher = new DominoRequestDispatcher();

    public GwtRequestAsyncSender(ServerRequestEventFactory requestEventFactory) {
        super(requestEventFactory);
        Defaults.setDispatcher(dispatcher);
    }

    @Override
    protected void sendRequest(ClientServerRequest request, ServerRequestEventFactory requestEventFactory) {
        ClientApp.make().getRequestRestSendersRepository().get(request.getKey())
                .send(request.arguments(), request.headers(),
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
