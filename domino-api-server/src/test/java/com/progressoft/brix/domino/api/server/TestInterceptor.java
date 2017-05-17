package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.server.interceptor.RequestInterceptor;

public class TestInterceptor implements RequestInterceptor<TestRequest, TestServerEntryPointContext> {
    @Override
    public void intercept(TestRequest request, TestServerEntryPointContext entryPoint) {
        request.appendTestWord("-intercepted");
        request.appendTestWord(entryPoint.getEntryContextParameter());
    }
}
