package com.progressoft.brix.domino.api.client.events;

import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;

public interface ServerRequestEventFactory {
    Event makeSuccess(ClientServerRequest request, ServerResponse serverResponse);
    Event makeFailed(ClientServerRequest request, Throwable error);
}
