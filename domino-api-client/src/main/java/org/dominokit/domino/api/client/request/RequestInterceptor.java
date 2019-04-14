package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.client.extension.ContextAggregator;

public interface RequestInterceptor {
    void interceptRequest(ServerRequest request, ContextAggregator.ContextWait<ServerRequest> contextWait);
}
