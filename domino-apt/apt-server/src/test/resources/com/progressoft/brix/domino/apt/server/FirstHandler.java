package com.progressoft.brix.domino.apt.server;

import com.progressoft.brix.domino.api.server.handler.Handler;
import com.progressoft.brix.domino.api.server.handler.RequestHandler;
import com.progressoft.brix.domino.api.server.context.ExecutionContext;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

@Handler(value = "somePath", classifier = "xyz")
public class FirstHandler implements RequestHandler<FirstRequest, ResponseBean> {
    @Override
    public void handleRequest(ExecutionContext<FirstRequest, ResponseBean> executionContext) {
    }

}
