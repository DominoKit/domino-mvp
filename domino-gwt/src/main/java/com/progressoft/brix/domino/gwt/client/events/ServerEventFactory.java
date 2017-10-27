package com.progressoft.brix.domino.gwt.client.events;

import com.progressoft.brix.domino.api.client.events.Event;
import com.progressoft.brix.domino.api.client.events.ServerRequestEventFactory;
import com.progressoft.brix.domino.api.client.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;

public class ServerEventFactory implements ServerRequestEventFactory {
    @Override
    public Event makeSuccess(ServerRequest request, ServerResponse serverResponse) {
        return new ServerSuccessRequestEvent(request, serverResponse);
    }

    @Override
    public Event makeFailed(ServerRequest request, Throwable error) {
        return new ServerFailedRequestEvent(request, error);
    }
}
