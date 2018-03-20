package org.dominokit.domino.apt.server;

import org.dominokit.domino.api.server.interceptor.Interceptor;
import org.dominokit.domino.api.server.interceptor.RequestInterceptor;
import org.dominokit.domino.api.server.request.RequestContext;

@Interceptor(FirstHandler.class)
public class FirstInterceptor implements RequestInterceptor<FirstRequest, TestServerEntryPointContext> {

    @Override
    public void intercept(RequestContext<FirstRequest> requestContext, TestServerEntryPointContext entryPoint) {
        //for code generation testing only
    }
}

