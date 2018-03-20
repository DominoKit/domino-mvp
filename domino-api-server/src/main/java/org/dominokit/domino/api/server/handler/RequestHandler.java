package org.dominokit.domino.api.server.handler;

import org.dominokit.domino.api.server.context.ExecutionContext;
import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.shared.request.ResponseBean;
import org.dominokit.domino.api.server.context.ExecutionContext;

@FunctionalInterface
public interface RequestHandler<R extends RequestBean, S extends ResponseBean> {
    void handleRequest(ExecutionContext<R, S> executionContext);
}
