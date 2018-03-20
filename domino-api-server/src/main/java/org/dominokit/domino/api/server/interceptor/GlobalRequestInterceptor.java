package org.dominokit.domino.api.server.interceptor;

import org.dominokit.domino.api.server.entrypoint.ServerEntryPointContext;
import org.dominokit.domino.api.server.request.RequestContext;
import org.dominokit.domino.api.server.entrypoint.ServerEntryPointContext;

@FunctionalInterface
public interface GlobalRequestInterceptor<E extends ServerEntryPointContext> {
    void intercept(RequestContext requestContext, E context);
}
