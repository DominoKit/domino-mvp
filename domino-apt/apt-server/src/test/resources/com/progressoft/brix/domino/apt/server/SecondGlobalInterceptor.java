package com.progressoft.brix.domino.apt.server;

import com.progressoft.brix.domino.api.server.interceptor.GlobalInterceptor;
import com.progressoft.brix.domino.api.server.interceptor.GlobalRequestInterceptor;
import com.progressoft.brix.domino.api.server.request.RequestContext;

@GlobalInterceptor
public class SecondGlobalInterceptor implements GlobalRequestInterceptor<TestServerEntryPointContext> {

    @Override
    public void intercept(RequestContext requestContext, TestServerEntryPointContext context) {
        //for generation testing only
    }
}

