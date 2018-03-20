package org.dominokit.domino.apt.server;

import org.dominokit.domino.api.server.handler.Handler;
import org.dominokit.domino.api.server.handler.RequestHandler;
import org.dominokit.domino.api.server.context.ExecutionContext;
import org.dominokit.domino.api.shared.request.ResponseBean;

@Handler("somePath2")
public class SecondHandler implements RequestHandler<SecondRequest, ResponseBean> {
    @Override
    public void handleRequest(ExecutionContext<SecondRequest, ResponseBean> executionContext) {
    }

}
