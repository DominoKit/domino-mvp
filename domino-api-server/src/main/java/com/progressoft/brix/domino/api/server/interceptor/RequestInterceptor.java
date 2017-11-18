package com.progressoft.brix.domino.api.server.interceptor;

import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointContext;
import com.progressoft.brix.domino.api.server.request.RequestContext;
import com.progressoft.brix.domino.api.shared.request.RequestBean;

@FunctionalInterface
public interface RequestInterceptor<R extends RequestBean, C extends ServerEntryPointContext> {
    void intercept(RequestContext<R> requestContext, C entryPoint);
}
