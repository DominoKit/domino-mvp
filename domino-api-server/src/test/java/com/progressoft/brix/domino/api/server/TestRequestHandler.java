package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.server.handler.RequestHandler;
import com.progressoft.brix.domino.api.server.request.RequestContext;

public class TestRequestHandler implements RequestHandler<TestRequest, TestResponse> {
    @Override
    public TestResponse handleRequest(RequestContext<TestRequest> requestContext) {
        requestContext.getRequestBean().appendTestWord("-handled");

        return new TestResponse();
    }
}
