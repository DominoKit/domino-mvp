package com.progressoft.brix.domino.apt.server;

import com.progressoft.brix.domino.api.server.interceptor.Interceptor;
import com.progressoft.brix.domino.api.server.interceptor.RequestInterceptor;
import com.progressoft.brix.domino.api.server.request.RequestContext;

@Interceptor(SecondHandler.class)
public class SecondInterceptor implements RequestInterceptor<SecondRequest, TestServerEntryPointContext> {

    @Override
    public void intercept(RequestContext<SecondRequest> requestContext, TestServerEntryPointContext entryPoint) {
        //for code generation testing only
    }
}

