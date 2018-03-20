package org.dominokit.domino.api.server.interceptor;

import org.dominokit.domino.api.server.entrypoint.ServerEntryPointContext;
import org.dominokit.domino.api.server.request.RequestContext;
import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.server.entrypoint.ServerEntryPointContext;

@FunctionalInterface
public interface RequestInterceptor<R extends RequestBean, C extends ServerEntryPointContext> {
    void intercept(RequestContext<R> requestContext, C entryPoint);
}
