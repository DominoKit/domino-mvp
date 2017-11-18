package com.progressoft.brix.domino.api.server.request;

import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointContext;
import com.progressoft.brix.domino.api.server.handler.CallbackRequestHandler;
import com.progressoft.brix.domino.api.server.handler.HandlersRepository;
import com.progressoft.brix.domino.api.server.interceptor.GlobalRequestInterceptor;
import com.progressoft.brix.domino.api.server.interceptor.InterceptorsRepository;
import com.progressoft.brix.domino.api.server.interceptor.RequestInterceptor;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

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
    public ResponseBean executeRequest(RequestContext requestContext, ServerEntryPointContext context) {
        callInterceptors(requestContext, context);
        return handlersRepository.findHandler(requestContext.getRequestBean().getRequestKey()).handleRequest(requestContext);
    }

    private void callInterceptors(RequestContext requestContext, ServerEntryPointContext context) {
        getInterceptors(requestContext, context).forEach(i -> i.intercept(requestContext, context));
        getGlobalInterceptors(context).forEach(i -> i.intercept(requestContext, context));
    }

    @Override
    public void executeCallbackRequest(RequestContext requestContext, ServerEntryPointContext context, CallbackRequestHandler.ResponseCallback<ResponseBean> responseCallback) {
        callInterceptors(requestContext, context);
        handlersRepository.findCallbackHandler(requestContext.getRequestBean().getRequestKey()).handleRequest(requestContext, responseCallback);
    }

    private Collection<RequestInterceptor> getInterceptors(RequestContext requestContext, ServerEntryPointContext context) {
        return interceptorsRepository.findInterceptors(requestContext.getRequestBean().getClass().getCanonicalName(), context.getClass().getCanonicalName());
    }

    private Collection<GlobalRequestInterceptor> getGlobalInterceptors(ServerEntryPointContext context) {
        return interceptorsRepository.findGlobalInterceptors(context.getClass().getCanonicalName());
    }
}
