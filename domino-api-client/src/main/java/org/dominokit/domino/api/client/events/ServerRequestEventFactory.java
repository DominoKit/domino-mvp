package org.dominokit.domino.api.client.events;

import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.shared.request.FailedResponseBean;

public interface ServerRequestEventFactory {
    <T> Event makeSuccess(ServerRequest request, T responseBean);
    Event makeFailed(ServerRequest request, FailedResponseBean failedResponse);
}
