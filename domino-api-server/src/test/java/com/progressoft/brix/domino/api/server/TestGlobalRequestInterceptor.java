package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.server.interceptor.GlobalRequestInterceptor;
import com.progressoft.brix.domino.api.shared.request.RequestBean;


public class TestGlobalRequestInterceptor implements GlobalRequestInterceptor<TestServerEntryPointContext> {
    @Override
    public void intercept(RequestBean request, TestServerEntryPointContext context) {
        ((TestRequest)request).appendTestWord("-globally-intercepted");
        ((TestRequest)request).appendTestWord(context.getEntryContextParameter());
    }
}
