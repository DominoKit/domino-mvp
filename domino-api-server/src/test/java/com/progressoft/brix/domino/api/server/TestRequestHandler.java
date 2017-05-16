package com.progressoft.brix.domino.api.server;

public class TestRequestHandler implements RequestHandler<TestRequest, TestResponse> {
    @Override
    public TestResponse handleRequest(TestRequest request) {
        request.appendTestWord("-handled");

        return new TestResponse();
    }
}
