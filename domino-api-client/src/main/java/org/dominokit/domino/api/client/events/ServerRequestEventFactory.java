package org.dominokit.domino.api.client.events;

import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.shared.request.FailedResponseBean;
import org.dominokit.domino.api.shared.request.ResponseBean;

public interface ServerRequestEventFactory {
    <T> Event makeSuccess(ServerRequest request, T responseBean);
    Event makeFailed(ServerRequest request, FailedResponseBean failedResponse);
}
