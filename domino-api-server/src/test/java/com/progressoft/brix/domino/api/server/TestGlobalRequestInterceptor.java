package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.server.interceptor.GlobalRequestInterceptor;
import com.progressoft.brix.domino.api.server.request.RequestContext;


public class TestGlobalRequestInterceptor implements GlobalRequestInterceptor<TestServerEntryPointContext> {
    @Override
    public void intercept(RequestContext requestContext, TestServerEntryPointContext context) {
        ((TestRequest) requestContext.getRequestBean()).appendTestWord("-globally-intercepted");
        ((TestRequest) requestContext.getRequestBean()).appendTestWord(context.getEntryContextParameter());
    }
}
