package com.progressoft.brix.domino.api.server.interceptor;

import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointContext;
import com.progressoft.brix.domino.api.shared.request.RequestBean;

@FunctionalInterface
public interface GlobalRequestInterceptor<E extends ServerEntryPointContext> {
    void intercept(RequestBean request, E context);
}
