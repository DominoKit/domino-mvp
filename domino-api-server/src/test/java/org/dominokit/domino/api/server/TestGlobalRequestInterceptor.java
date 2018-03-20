package org.dominokit.domino.api.server;

import org.dominokit.domino.api.server.interceptor.GlobalRequestInterceptor;
import org.dominokit.domino.api.server.request.RequestContext;


public class TestGlobalRequestInterceptor implements GlobalRequestInterceptor<TestServerEntryPointContext> {
    @Override
    public void intercept(RequestContext requestContext, TestServerEntryPointContext context) {
        ((TestRequest) requestContext.getRequestBean()).appendTestWord("-globally-intercepted");
        ((TestRequest) requestContext.getRequestBean()).appendTestWord(context.getEntryContextParameter());
    }
}
