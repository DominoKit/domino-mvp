package com.progressoft.brix.domino.apt.server.callbackHandler;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.server.ServerModuleConfiguration;
import com.progressoft.brix.domino.api.server.HandlerRegistry;
import com.progressoft.brix.domino.apt.server.callbackHandler.HandlerImplementingRequestHandlerInterface;
import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.server.EndpointsRegistry;
import com.progressoft.brix.domino.apt.server.callbackHandler.HandlerImplementingRequestHandlerInterfaceEndpointHandler;

@AutoService(ServerModuleConfiguration.class)
public class TestServerModule implements ServerModuleConfiguration{

    @Override
    public void registerHandlers(HandlerRegistry registry) {
        registry.registerCallbackHandler(ServerRequest.class.getCanonicalName(), new HandlerImplementingRequestHandlerInterface());
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