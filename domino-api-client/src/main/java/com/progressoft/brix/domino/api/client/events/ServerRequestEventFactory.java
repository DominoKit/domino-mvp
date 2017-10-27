package com.progressoft.brix.domino.api.client.events;

import com.progressoft.brix.domino.api.client.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;

public interface ServerRequestEventFactory {
    Event makeSuccess(ServerRequest request, ServerResponse serverResponse);
    Event makeFailed(ServerRequest request, Throwable error);
}
