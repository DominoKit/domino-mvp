package org.dominokit.domino.gwt.client.events;

import org.dominokit.domino.api.client.events.Event;
import org.dominokit.domino.api.client.events.ServerRequestEventFactory;
import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.shared.request.ResponseBean;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ServerEventFactory implements ServerRequestEventFactory {
    @Override
    public Event makeSuccess(ServerRequest request, ResponseBean responseBean) {
        return new ServerSuccessRequestEvent(request, responseBean);
    }

    @Override
    public Event makeFailed(ServerRequest request, Throwable error) {
        return new ServerFailedRequestEvent(request, error);
    }
}
