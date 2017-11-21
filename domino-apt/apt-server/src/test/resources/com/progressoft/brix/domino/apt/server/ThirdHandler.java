package com.progressoft.brix.domino.apt.server;

import com.progressoft.brix.domino.api.server.handler.Handler;
import com.progressoft.brix.domino.api.server.handler.RequestHandler;
import com.progressoft.brix.domino.api.server.context.ExecutionContext;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

@Handler("somePath3")
public class ThirdHandler implements RequestHandler<ThirdRequest, ResponseBean> {
    @Override
    public void handleRequest(ExecutionContext<ThirdRequest, ResponseBean> executionContext) {
    }

}
