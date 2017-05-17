package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.server.handler.RequestHandler;

public class TestRequestHandler implements RequestHandler<TestRequest, TestResponse> {
    @Override
    public TestResponse handleRequest(TestRequest request) {
        request.appendTestWord("-handled");

        return new TestResponse();
    }
}
