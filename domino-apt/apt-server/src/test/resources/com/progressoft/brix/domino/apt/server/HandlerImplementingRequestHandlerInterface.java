package com.progressoft.brix.domino.apt.server;

import com.progressoft.brix.domino.api.server.handler.Handler;
import com.progressoft.brix.domino.api.server.handler.RequestHandler;
import com.progressoft.brix.domino.api.server.context.ExecutionContext;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

@Handler("somePath")
public class HandlerImplementingRequestHandlerInterface implements RequestHandler<RequestBean, ResponseBean> {
    @Override
    public void handleRequest(ExecutionContext<RequestBean, ResponseBean> executionContext) {
    }

}
