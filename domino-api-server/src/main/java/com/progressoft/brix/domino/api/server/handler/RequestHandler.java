package com.progressoft.brix.domino.api.server.handler;

import com.progressoft.brix.domino.api.server.request.RequestContext;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

@FunctionalInterface
public interface RequestHandler<R extends RequestBean, S extends ResponseBean> {
    S handleRequest(RequestContext<R> requestContext);
}
