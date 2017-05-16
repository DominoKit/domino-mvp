package com.progressoft.brix.domino.api.server;

import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;

import java.util.Objects;

import static java.util.Objects.isNull;

public class ServerApp implements HandlerRegistry, InterceptorsRegistry, EndpointsRegistry{

    private static AttributeHolder<RequestExecutor> requestExecutorHolder = new AttributeHolder<>();
    private static AttributeHolder<HandlersRepository> handlersRepositoryHolder = new AttributeHolder<>();
    private static AttributeHolder<InterceptorsRepository> interceptorsRepositoryHolder = new AttributeHolder<>();
    private static AttributeHolder<ServerContext> serverContextHolder=new AttributeHolder<>();

    private ServerApp() {
    }

    public static ServerApp make(){
        return new ServerApp();
    }

    public ServerApp run() {
        return this;
    }

    public ServerResponse executeRequest(ServerRequest request, ServerEntryPointContext context) {
        return requestExecutorHolder.attribute.executeRequest(request, context);
    }
    public void executeCallbackRequest(ServerRequest request, ServerEntryPointContext context, CallbackRequestHandler.ResponseCallback responseCallback) {
        requestExecutorHolder.attribute.executeCallbackRequest(request, context, responseCallback);
    }


    @Override
    public void registerHandler(String request, RequestHandler handler) {
        handlersRepositoryHolder.attribute.registerHandler(request, handler);
    }

    @Override
    public void registerCallbackHandler(String request, CallbackRequestHandler handler) {
        handlersRepositoryHolder.attribute.registerCallbackHandler(request, handler);
    }

    @Override
    public void registerEndpoint(String path, EndpointHandlerFactory factory) {
        serverContextHolder.attribute.publishService(path, factory);
    }

    public HandlersRepository handlersRepository() {
        return handlersRepositoryHolder.attribute;
    }

    public ServerContext serverContext(){
        return serverContextHolder.attribute;
    }

    @Override
    public void registerInterceptor(String requestName, String entryPointName, RequestInterceptor interceptor) {
        interceptorsRepositoryHolder.attribute.addInterceptor(requestName, entryPointName, interceptor);
    }

    @Override
    public void registerGlobalInterceptor(String entryPointName, GlobalRequestInterceptor interceptor) {
        interceptorsRepositoryHolder.attribute.addGlobalInterceptor(entryPointName, interceptor);
    }

    public void configureModule(ServerModuleConfiguration configuration) {
        configuration.registerHandlers(this);
        configuration.registerInterceptors(this);
        configuration.registerGlobalInterceptors(this);
        configuration.registerEndpoints(this);
    }

    public static class ServerAppBuilder{

        private RequestExecutor requestExecutor;
        private HandlersRepository handlersRepository;
        private InterceptorsRepository interceptorsRepository;
        private ServerContext serverContext;

        public ServerAppBuilder executor(RequestExecutor requestExecutor){
            this.requestExecutor=requestExecutor;
            return this;
        }

        public ServerAppBuilder handlersRepository(HandlersRepository handlersRepository){
            this.handlersRepository=handlersRepository;
            return this;
        }

        public ServerAppBuilder interceptorsRepository(InterceptorsRepository interceptorsRepository){
            this.interceptorsRepository=interceptorsRepository;
            return this;
        }

        public ServerAppBuilder serverContext(ServerContext serverContext){
            this.serverContext=serverContext;
            return this;
        }

        public ServerApp build(){
            if(Objects.isNull(requestExecutor))
                throw new RequestExecutorIsRequired();
            if(Objects.isNull(handlersRepository))
                throw new HandlersRepositoryIsRequired();
            if(Objects.isNull(interceptorsRepository))
                throw new InterceptorsRepositoryIsRequired();
            if(isNull(serverContext))
                throw new ServerContextIsRequired();

            ServerApp.requestExecutorHolder.hold(requestExecutor);
            ServerApp.handlersRepositoryHolder.hold(handlersRepository);
            ServerApp.interceptorsRepositoryHolder.hold(interceptorsRepository);
            ServerApp.serverContextHolder.hold(serverContext);

            return new ServerApp();
        }

        private class RequestExecutorIsRequired extends RuntimeException {
        }

        private class HandlersRepositoryIsRequired extends RuntimeException {
        }

        private class InterceptorsRepositoryIsRequired extends RuntimeException {
        }

        private class ServerContextIsRequired extends RuntimeException {
        }
    }

    private static final class AttributeHolder<T>{
        private T attribute;

        public void hold(T attribute){
            this.attribute=attribute;
        }
    }
}
