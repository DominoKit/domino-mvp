package com.progressoft.brix.domino.apt.server.multiMix;

import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointContext;
import com.progressoft.brix.domino.api.server.interceptor.Interceptor;
import com.progressoft.brix.domino.api.server.interceptor.RequestInterceptor;
import com.progressoft.brix.domino.api.server.request.RequestContext;

@Interceptor
public class SecondInterceptor implements RequestInterceptor<SecondRequest, TestServerEntryPointContext>{

    @Override
    public void intercept(RequestContext<SecondRequest> requestContext, TestServerEntryPointContext entryPoint) {
        //for generation testing only
    }
}

