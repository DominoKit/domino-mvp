package com.progressoft.brix.domino.apt.server.singleHandler;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.server.config.ServerModuleConfiguration;
import com.progressoft.brix.domino.api.server.endpoint.EndpointsRegistry;
import com.progressoft.brix.domino.api.server.handler.HandlerRegistry;
import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import java.lang.Override;
import javax.annotation.Generated;

@Generated("com.progressoft.brix.domino.apt.server.ServerModuleAnnotationProcessor")
@AutoService(ServerModuleConfiguration.class)
public class TestServerModule implements ServerModuleConfiguration{

    @Override
    public void registerHandlers(HandlerRegistry registry) {
        registry.registerHandler(ServerRequest.class.getCanonicalName(), new HandlerImplementingRequestHandlerInterface());
    }

    @Override
    public void registerEndpoints(EndpointsRegistry registry) {
        registry.registerEndpoint("somePath", new EndpointsRegistry.EndpointHandlerFactory() {
            @Override
            public HandlerImplementingRequestHandlerInterfaceEndpointHandler get() {
                return new HandlerImplementingRequestHandlerInterfaceEndpointHandler();
            }
        });
    }
}