package org.dominokit.domino.api.server;

import org.dominokit.domino.api.server.config.ServerModuleConfiguration;
import org.dominokit.domino.api.server.context.ExecutionContext;
import org.dominokit.domino.api.server.endpoint.EndpointsRegistry;
import org.dominokit.domino.api.server.entrypoint.ServerContext;
import org.dominokit.domino.api.server.entrypoint.ServerEntryPointContext;
import org.dominokit.domino.api.server.handler.HandlerRegistry;
import org.dominokit.domino.api.server.handler.HandlersRepository;
import org.dominokit.domino.api.server.handler.RequestHandler;
import org.dominokit.domino.api.server.interceptor.GlobalRequestInterceptor;
import org.dominokit.domino.api.server.interceptor.InterceptorsRegistry;
import org.dominokit.domino.api.server.interceptor.InterceptorsRepository;
import org.dominokit.domino.api.server.interceptor.RequestInterceptor;
import org.dominokit.domino.api.server.request.RequestExecutor;

import java.util.function.Supplier;

import static java.util.Objects.isNull;

public class ServerApp implements HandlerRegistry, InterceptorsRegistry, EndpointsRegistry {

    private static AttributeHolder<RequestExecutor> requestExecutorHolder = new AttributeHolder<>();
    private static AttributeHolder<HandlersRepository> handlersRepositoryHolder = new AttributeHolder<>();
    private static AttributeHolder<InterceptorsRepository> interceptorsRepositoryHolder = new AttributeHolder<>();
    private static AttributeHolder<ServerContext> serverContextHolder = new AttributeHolder<>();

    private ServerApp() {
    }

    public static ServerApp make() {
        return new ServerApp();
    }

    public ServerApp run() {
        return this;
    }

    public void executeRequest(ExecutionContext requestContext, ServerEntryPointContext context) {
        requestExecutorHolder.attribute.executeRequest(requestContext, context);
    }

    @Override
    public void registerHandler(String path, RequestHandler handler) {
        handlersRepositoryHolder.attribute.registerHandler("/service/" + path, handler);
    }

    @Override
    public void registerEndpoint(String path, Supplier<?> factory) {
        serverContextHolder.attribute.publishService(path, factory);
    }

    public HandlersRepository handlersRepository() {
        return handlersRepositoryHolder.attribute;
    }

    public ServerContext serverContext() {
        return serverContextHolder.attribute;
    }

    @Override
    public void registerInterceptor(String handlerName, String entryPointName, RequestInterceptor interceptor) {
        interceptorsRepositoryHolder.attribute.addInterceptor(handlerName, entryPointName, interceptor);
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

    public static class ServerAppBuilder {

        private RequestExecutor requestExecutor;
        private HandlersRepository handlersRepository;
        private InterceptorsRepository interceptorsRepository;
        private ServerContext serverContext;

        public ServerAppBuilder executor(RequestExecutor requestExecutor) {
            this.requestExecutor = requestExecutor;
            return this;
        }

        public ServerAppBuilder handlersRepository(HandlersRepository handlersRepository) {
            this.handlersRepository = handlersRepository;
            return this;
        }

        public ServerAppBuilder interceptorsRepository(InterceptorsRepository interceptorsRepository) {
            this.interceptorsRepository = interceptorsRepository;
            return this;
        }

        public ServerAppBuilder serverContext(ServerContext serverContext) {
            this.serverContext = serverContext;
            return this;
        }

        public ServerApp build() {
            if (isNull(requestExecutor))
                throw new RequestExecutorIsRequired();
            if (isNull(handlersRepository))
                throw new HandlersRepositoryIsRequired();
            if (isNull(interceptorsRepository))
                throw new InterceptorsRepositoryIsRequired();
            if (isNull(serverContext))
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

    private static final class AttributeHolder<T> {
        private T attribute;

        private void hold(T attribute) {
            this.attribute = attribute;
        }
    }
}
