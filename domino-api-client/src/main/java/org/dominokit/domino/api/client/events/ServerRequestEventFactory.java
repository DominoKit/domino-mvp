package org.dominokit.domino.api.client.events;

import org.dominokit.domino.api.shared.request.FailedResponseBean;
import org.dominokit.domino.api.shared.request.ServerRequest;

public interface ServerRequestEventFactory {
    <T> Event makeSuccess(ServerRequest request, T responseBean);
    Event makeFailed(ServerRequest request, FailedResponseBean failedResponse);
}
