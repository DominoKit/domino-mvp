package org.dominokit.domino.api.server;

import org.dominokit.domino.api.server.context.ExecutionContext;
import org.dominokit.domino.api.server.handler.RequestHandler;

public class TestRequestHandler implements RequestHandler<TestRequest, TestResponse> {
    @Override
    public void handleRequest(ExecutionContext<TestRequest, TestResponse> executionContext) {
        executionContext.getRequestBean().appendTestWord("-handled");
        executionContext.end(new TestResponse());
    }
}
