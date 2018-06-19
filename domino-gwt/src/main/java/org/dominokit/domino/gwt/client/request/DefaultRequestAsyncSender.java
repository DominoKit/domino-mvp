package org.dominokit.domino.gwt.client.request;

import org.dominokit.domino.api.client.events.ServerRequestEventFactory;
import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.client.commons.request.AbstractRequestAsyncSender;


public class DefaultRequestAsyncSender extends AbstractRequestAsyncSender {

    public DefaultRequestAsyncSender(ServerRequestEventFactory requestEventFactory) {
        super(requestEventFactory);
    }

    @Override
    protected void sendRequest(ServerRequest request, ServerRequestEventFactory requestEventFactory) {
        throw new NotImplementedException();
    }

    private final static class NotImplementedException extends RuntimeException{

    }
}
