package com.progressoft.brix.domino.apt.server.singleGlobalInterceptor;

import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointContext;
import com.progressoft.brix.domino.api.server.request.RequestContext;
import com.progressoft.brix.domino.api.server.interceptor.GlobalInterceptor;
import com.progressoft.brix.domino.api.server.interceptor.GlobalRequestInterceptor;

@GlobalInterceptor
public class FirstGlobalInterceptor implements GlobalRequestInterceptor<TestServerEntryPointContext>{

    @Override
    public void intercept(RequestContext requestContext, TestServerEntryPointContext context) {
        //for generation testing only
    }
}

