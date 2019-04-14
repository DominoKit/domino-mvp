package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.client.extension.ContextAggregator;

public class InterceptorRequestWait extends ContextAggregator.ContextWait<ServerRequest> {

    private final RequestInterceptor interceptor;

    public InterceptorRequestWait(RequestInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public RequestInterceptor getInterceptor() {
        return interceptor;
    }
}
