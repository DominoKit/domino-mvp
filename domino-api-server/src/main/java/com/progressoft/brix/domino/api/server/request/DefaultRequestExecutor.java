package com.progressoft.brix.domino.api.server.request;

import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointContext;
import com.progressoft.brix.domino.api.server.handler.CallbackRequestHandler;
import com.progressoft.brix.domino.api.server.handler.HandlersRepository;
import com.progressoft.brix.domino.api.server.interceptor.GlobalRequestInterceptor;
import com.progressoft.brix.domino.api.server.interceptor.InterceptorsRepository;
import com.progressoft.brix.domino.api.server.interceptor.RequestInterceptor;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
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
    public ResponseBean executeRequest(RequestBean request, ServerEntryPointContext context) {
        callInterceptors(request, context);
        return handlersRepository.findHandler(request.getRequestKey()).handleRequest(request);
    }

    private void callInterceptors(RequestBean request, ServerEntryPointContext context) {
        getInterceptors(request, context).forEach(i -> i.intercept(request, context));
        getGlobalInterceptors(context).forEach(i -> i.intercept(request, context));
    }

    @Override
    public void executeCallbackRequest(RequestBean request, ServerEntryPointContext context, CallbackRequestHandler.ResponseCallback<ResponseBean> responseCallback) {
        callInterceptors(request, context);
        handlersRepository.findCallbackHandler(request.getRequestKey()).handleRequest(request, responseCallback);
    }

    private Collection<RequestInterceptor> getInterceptors(RequestBean request, ServerEntryPointContext context) {
        return interceptorsRepository.findInterceptors(request.getClass().getCanonicalName(), context.getName());
    }

    private Collection<GlobalRequestInterceptor> getGlobalInterceptors(ServerEntryPointContext context) {
        return interceptorsRepository.findGlobalInterceptors(context.getClass().getCanonicalName());
    }
}
