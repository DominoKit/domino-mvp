package org.dominokit.domino.gwt.client.events;

import org.dominokit.domino.api.client.events.Event;
import org.dominokit.domino.api.client.events.ServerRequestEventFactory;
import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.shared.request.FailedResponseBean;
import org.dominokit.domino.api.shared.request.ResponseBean;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ServerEventFactory implements ServerRequestEventFactory {
    @Override
    public <T> Event makeSuccess(ServerRequest request, T responseBean) {
        return new ServerSuccessRequestEvent(request, responseBean);
    }

    @Override
    public Event makeFailed(ServerRequest request, FailedResponseBean failedResponseBean) {
        return new ServerFailedRequestEvent(request, failedResponseBean);
    }
}
