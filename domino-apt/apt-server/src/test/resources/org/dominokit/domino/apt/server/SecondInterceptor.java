package org.dominokit.domino.apt.server;

import org.dominokit.domino.api.server.interceptor.Interceptor;
import org.dominokit.domino.api.server.interceptor.RequestInterceptor;
import org.dominokit.domino.api.server.request.RequestContext;

@Interceptor(SecondHandler.class)
public class SecondInterceptor implements RequestInterceptor<SecondRequest, TestServerEntryPointContext> {

    @Override
    public void intercept(RequestContext<SecondRequest> requestContext, TestServerEntryPointContext entryPoint) {
        //for code generation testing only
    }
}

