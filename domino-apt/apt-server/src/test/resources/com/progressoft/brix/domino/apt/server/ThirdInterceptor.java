package com.progressoft.brix.domino.apt.server;

import com.progressoft.brix.domino.api.server.interceptor.Interceptor;
import com.progressoft.brix.domino.api.server.interceptor.RequestInterceptor;
import com.progressoft.brix.domino.api.server.request.RequestContext;

@Interceptor(ThirdHandler.class)
public class ThirdInterceptor implements RequestInterceptor<ThirdRequest, TestServerEntryPointContext> {

    @Override
    public void intercept(RequestContext<ThirdRequest> requestContext, TestServerEntryPointContext entryPoint) {
        //for code generation testing only
    }
}

