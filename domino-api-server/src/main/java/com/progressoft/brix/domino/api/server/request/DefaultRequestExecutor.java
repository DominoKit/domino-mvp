package com.progressoft.brix.domino.api.server.request;

import com.progressoft.brix.domino.api.server.context.ExecutionContext;
import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointContext;
import com.progressoft.brix.domino.api.server.handler.HandlersRepository;
import com.progressoft.brix.domino.api.server.interceptor.GlobalRequestInterceptor;
import com.progressoft.brix.domino.api.server.interceptor.InterceptorsRepository;
import com.progressoft.brix.domino.api.server.interceptor.RequestInterceptor;

import java.util.Collection;

public class DefaultRequestExecutor implements RequestExecutor {

    private final HandlersRepository handlersRepository;
    private final InterceptorsRepository interceptorsRepository;

    public DefaultRequestExecutor(HandlersRepository handlersRepository,
                                  InterceptorsRepository interceptorsRepository) {
        this.handlersRepository = handlersRepository;
        this.interceptorsRepository = interceptorsRepository;
    }

    @Override
    public void executeRequest(ExecutionContext requestContext, ServerEntryPointContext context) {
        callInterceptors(requestContext, context);
        handlersRepository.findHandler(requestContext.request().getRequestKey()).handleRequest(requestContext);
    }

    private void callInterceptors(ExecutionContext requestContext, ServerEntryPointContext context) {
        getInterceptors(requestContext, context).forEach(i -> i.intercept(requestContext.request(), context));
        getGlobalInterceptors(context).forEach(i -> i.intercept(requestContext.request(), context));
    }

    private Collection<RequestInterceptor> getInterceptors(ExecutionContext requestContext, ServerEntryPointContext context) {
        return interceptorsRepository.findInterceptors(requestContext.request().getRequestBean().getClass().getCanonicalName(), context.getClass().getCanonicalName());
    }

    private Collection<GlobalRequestInterceptor> getGlobalInterceptors(ServerEntryPointContext context) {
        return interceptorsRepository.findGlobalInterceptors(context.getClass().getCanonicalName());
    }
}
