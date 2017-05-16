package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.shared.request.ServerRequest;


public class TestGlobalRequestInterceptor implements GlobalRequestInterceptor<TestServerEntryPointContext> {
    @Override
    public void intercept(ServerRequest request, TestServerEntryPointContext context) {
        ((TestRequest)request).appendTestWord("-globally-intercepted");
        ((TestRequest)request).appendTestWord(context.getEntryContextParameter());
    }
}
