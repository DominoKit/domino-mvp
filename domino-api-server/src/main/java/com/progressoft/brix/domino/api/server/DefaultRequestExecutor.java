package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;

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
    public ServerResponse executeRequest(ServerRequest request, ServerEntryPointContext context) {
        callInterceptors(request, context);
        return handlersRepository.findHandler(request.getClass().getCanonicalName()).handleRequest(request);
    }

    private void callInterceptors(ServerRequest request, ServerEntryPointContext context) {
        getInterceptors(request, context).forEach(i -> i.intercept(request, context));
        getGlobalInterceptors(context).forEach(i -> i.intercept(request, context));
    }

    @Override
    public void executeCallbackRequest(ServerRequest request, ServerEntryPointContext context, CallbackRequestHandler.ResponseCallback<ServerResponse> responseCallback) {
        callInterceptors(request, context);
        handlersRepository.findCallbackHandler(request.getClass().getCanonicalName()).handleRequest(request, responseCallback);
    }

    private Collection<RequestInterceptor> getInterceptors(ServerRequest request, ServerEntryPointContext context) {
        return interceptorsRepository.findInterceptors(request.getClass().getCanonicalName(), context.getName());
    }

    private Collection<GlobalRequestInterceptor> getGlobalInterceptors(ServerEntryPointContext context) {
        return interceptorsRepository.findGlobalInterceptors(context.getClass().getCanonicalName());
    }
}
