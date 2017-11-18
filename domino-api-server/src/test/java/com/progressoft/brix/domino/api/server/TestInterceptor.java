package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.server.interceptor.RequestInterceptor;
import com.progressoft.brix.domino.api.server.request.RequestContext;

public class TestInterceptor implements RequestInterceptor<TestRequest, TestServerEntryPointContext> {
    @Override
    public void intercept(RequestContext<TestRequest> requestContext, TestServerEntryPointContext entryPoint) {
        requestContext.getRequestBean().appendTestWord("-intercepted");
        requestContext.getRequestBean().appendTestWord(entryPoint.getEntryContextParameter());
    }
}
