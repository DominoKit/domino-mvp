package com.progressoft.brix.domino.api.server.handler;

import com.progressoft.brix.domino.api.server.request.RequestContext;
import com.progressoft.brix.domino.api.shared.request.RedirectableResponse;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

@FunctionalInterface
public interface CallbackRequestHandler<R extends RequestBean, S extends ResponseBean> {
    void handleRequest(RequestContext<R> requestContext, ResponseCallback<S> responseCallback);

    interface ResponseCallback<S> extends RedirectableResponse {
        void complete(S response);
    }
}
