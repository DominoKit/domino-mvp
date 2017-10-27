package com.progressoft.brix.domino.api.server.handler;

import com.progressoft.brix.domino.api.shared.request.RedirectableResponse;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

@FunctionalInterface
public interface CallbackRequestHandler<R extends RequestBean, S extends ResponseBean> {
    void handleRequest(R request, ResponseCallback<S> responseCallback);

    interface ResponseCallback<S> extends RedirectableResponse {
        void complete(S response);
    }
}
