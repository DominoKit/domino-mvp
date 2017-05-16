package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.shared.request.ServerRequest;

@FunctionalInterface
public interface GlobalRequestInterceptor<E extends ServerEntryPointContext> {
    void intercept(ServerRequest request, E context);
}
