package com.progressoft.brix.domino.api.server.interceptor;

import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointContext;
import com.progressoft.brix.domino.api.shared.request.ServerRequest;

@FunctionalInterface
public interface RequestInterceptor<R extends ServerRequest, C extends ServerEntryPointContext> {
    void intercept(R request, C entryPoint);
}
