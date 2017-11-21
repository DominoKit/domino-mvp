package com.progressoft.brix.domino.api.server.context;

import com.progressoft.brix.domino.api.server.request.RequestContext;
import com.progressoft.brix.domino.api.server.response.ResponseContext;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

public class DefaultExecutionContext<T extends RequestBean, S extends ResponseBean> implements ExecutionContext<T, S> {

    private final RequestContext<T> requestContext;
    private final ResponseContext<S> responseContext;

    public DefaultExecutionContext(RequestContext<T> requestContext, ResponseContext<S> responseContext) {
        this.requestContext = requestContext;
        this.responseContext = responseContext;
    }

    @Override
    public RequestContext<T> request() {
        return requestContext;
    }

    @Override
    public ResponseContext<S> response() {
        return responseContext;
    }
}
