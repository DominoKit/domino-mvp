package org.dominokit.domino.api.client.events;

import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.shared.request.ResponseBean;

public interface ServerRequestEventFactory {
    Event makeSuccess(ServerRequest request, ResponseBean responseBean);
    Event makeFailed(ServerRequest request, Throwable error);
}
