package org.dominokit.domino.api.server.request;

import org.dominokit.domino.api.server.context.ExecutionContext;
import org.dominokit.domino.api.server.entrypoint.ServerEntryPointContext;
import org.dominokit.domino.api.server.handler.HandlersRepository;
import org.dominokit.domino.api.server.handler.RequestHandler;
import org.dominokit.domino.api.server.interceptor.GlobalRequestInterceptor;
import org.dominokit.domino.api.server.interceptor.InterceptorsRepository;
import org.dominokit.domino.api.server.interceptor.RequestInterceptor;
import org.dominokit.domino.api.server.context.ExecutionContext;
import org.dominokit.domino.api.server.entrypoint.ServerEntryPointContext;
import org.dominokit.domino.api.server.handler.HandlersRepository;
import org.dominokit.domino.api.server.handler.RequestHandler;
import org.dominokit.domino.api.server.interceptor.GlobalRequestInterceptor;
import org.dominokit.domino.api.server.interceptor.InterceptorsRepository;
import org.dominokit.domino.api.server.interceptor.RequestInterceptor;

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
    public void executeRequest(ExecutionContext executionContext, ServerEntryPointContext context) {
        RequestHandler handler = handlersRepository.findHandler(executionContext.getRequestPath());
        callInterceptors(handler, executionContext, context);
        handler.handleRequest(executionContext);
    }

    private void callInterceptors(RequestHandler handler, RequestContext requestContext, ServerEntryPointContext context) {
        getInterceptors(handler, context).forEach(i -> i.intercept(requestContext, context));
        getGlobalInterceptors(context).forEach(i -> i.intercept(requestContext, context));
    }

    private Collection<RequestInterceptor> getInterceptors(RequestHandler handler, ServerEntryPointContext context) {
        return interceptorsRepository.findInterceptors(handler.getClass().getCanonicalName(), context.getClass().getCanonicalName());
    }

    private Collection<GlobalRequestInterceptor> getGlobalInterceptors(ServerEntryPointContext context) {
        return interceptorsRepository.findGlobalInterceptors(context.getClass().getCanonicalName());
    }
}
