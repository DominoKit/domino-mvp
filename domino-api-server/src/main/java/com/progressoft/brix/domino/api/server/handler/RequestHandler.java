package com.progressoft.brix.domino.api.server.handler;

import com.progressoft.brix.domino.api.server.context.ExecutionContext;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

@FunctionalInterface
public interface RequestHandler<R extends RequestBean, S extends ResponseBean> {
    void handleRequest(ExecutionContext<R, S> executionContext);
}
