package com.progressoft.brix.domino.api.client.events;

import com.progressoft.brix.domino.api.client.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

public interface ServerRequestEventFactory {
    Event makeSuccess(ServerRequest request, ResponseBean responseBean);
    Event makeFailed(ServerRequest request, Throwable error);
}
