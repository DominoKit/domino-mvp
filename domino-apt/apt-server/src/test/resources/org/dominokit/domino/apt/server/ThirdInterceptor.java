package org.dominokit.domino.apt.server;

import org.dominokit.domino.api.server.interceptor.Interceptor;
import org.dominokit.domino.api.server.interceptor.RequestInterceptor;
import org.dominokit.domino.api.server.request.RequestContext;

@Interceptor(ThirdHandler.class)
public class ThirdInterceptor implements RequestInterceptor<ThirdRequest, TestServerEntryPointContext> {

    @Override
    public void intercept(RequestContext<ThirdRequest> requestContext, TestServerEntryPointContext entryPoint) {
        //for code generation testing only
    }
}

