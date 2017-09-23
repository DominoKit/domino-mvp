package com.progressoft.brix.domino.gwt.client.request;

import com.progressoft.brix.domino.api.client.events.ServerRequestEventFactory;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.client.commons.request.AbstractRequestAsyncSender;
import org.fusesource.restygwt.client.Defaults;

public class GwtRequestAsyncSender extends AbstractRequestAsyncSender {

    private final DominoRequestDispatcher dispatcher = new DominoRequestDispatcher();

    public GwtRequestAsyncSender(ServerRequestEventFactory requestEventFactory) {
        super(requestEventFactory);
        Defaults.setDispatcher(dispatcher);
    }

    @Override
    public void onBeforeSend(ClientServerRequest request) {
        dispatcher.withHeaders(request.headers());
    }
}
