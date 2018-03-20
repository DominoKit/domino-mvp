package org.dominokit.domino.apt.server;

import org.dominokit.domino.api.server.request.RequestContext;
import org.dominokit.domino.api.server.interceptor.GlobalInterceptor;
import org.dominokit.domino.api.server.interceptor.GlobalRequestInterceptor;

@GlobalInterceptor
public class FirstGlobalInterceptor implements GlobalRequestInterceptor<TestServerEntryPointContext>{

    @Override
    public void intercept(RequestContext requestContext, TestServerEntryPointContext context) {
        //for generation testing only
    }
}

