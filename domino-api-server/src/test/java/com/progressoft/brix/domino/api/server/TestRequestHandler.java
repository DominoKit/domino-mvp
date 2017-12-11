package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.server.context.ExecutionContext;
import com.progressoft.brix.domino.api.server.handler.RequestHandler;

public class TestRequestHandler implements RequestHandler<TestRequest, TestResponse> {
    @Override
    public void handleRequest(ExecutionContext<TestRequest, TestResponse> executionContext) {
        executionContext.getRequestBean().appendTestWord("-handled");
        executionContext.end(new TestResponse());
    }
}
