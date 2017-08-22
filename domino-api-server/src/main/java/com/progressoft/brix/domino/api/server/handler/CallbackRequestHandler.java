package com.progressoft.brix.domino.api.server.handler;

import com.progressoft.brix.domino.api.shared.request.RedirectableResponse;
import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;

@FunctionalInterface
public interface CallbackRequestHandler<R extends ServerRequest, S extends ServerResponse> {
    void handleRequest(R request, ResponseCallback<S> responseCallback);

    interface ResponseCallback<S> extends RedirectableResponse {
        void complete(S response);
    }
}
