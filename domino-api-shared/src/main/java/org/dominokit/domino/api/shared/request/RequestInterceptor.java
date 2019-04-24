package org.dominokit.domino.api.shared.request;


import org.dominokit.domino.api.shared.extension.ContextAggregator;

public interface RequestInterceptor {
    void interceptRequest(ServerRequest request, ContextAggregator.ContextWait<ServerRequest> contextWait);
}
