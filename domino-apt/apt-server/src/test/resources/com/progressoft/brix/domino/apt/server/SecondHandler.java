package com.progressoft.brix.domino.apt.server;

import com.progressoft.brix.domino.api.server.handler.Handler;
import com.progressoft.brix.domino.api.server.handler.RequestHandler;
import com.progressoft.brix.domino.api.server.context.ExecutionContext;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

@Handler("somePath2")
public class SecondHandler implements RequestHandler<SecondRequest, ResponseBean> {
    @Override
    public void handleRequest(ExecutionContext<SecondRequest, ResponseBean> executionContext) {
    }

}
