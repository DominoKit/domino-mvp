package org.dominokit.domino.apt.server;

import org.dominokit.domino.api.server.handler.Handler;
import org.dominokit.domino.api.server.handler.RequestHandler;
import org.dominokit.domino.api.server.context.ExecutionContext;
import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.shared.request.ResponseBean;

@Handler("somePath")
public class HandlerImplementingRequestHandlerInterface implements RequestHandler<RequestBean, ResponseBean> {
    @Override
    public void handleRequest(ExecutionContext<RequestBean, ResponseBean> executionContext) {
    }

}
