package com.progressoft.brix.domino.apt.server.multiHandlers;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.server.config.ServerModuleConfiguration;
import com.progressoft.brix.domino.api.server.handler.HandlerRegistry;
import com.progressoft.brix.domino.apt.server.multiHandlers.FirstHandler;
import com.progressoft.brix.domino.apt.server.multiHandlers.FirstRequest;
import com.progressoft.brix.domino.apt.server.multiHandlers.SecondHandler;
import com.progressoft.brix.domino.apt.server.multiHandlers.SecondRequest;
import com.progressoft.brix.domino.apt.server.multiHandlers.ThirdHandler;
import com.progressoft.brix.domino.apt.server.multiHandlers.ThirdRequest;
import com.progressoft.brix.domino.api.server.endpoint.EndpointsRegistry;
import com.progressoft.brix.domino.apt.server.multiHandlers.FirstHandlerEndpointHandler;
import com.progressoft.brix.domino.apt.server.multiHandlers.SecondHandlerEndpointHandler;
import com.progressoft.brix.domino.apt.server.multiHandlers.ThirdHandlerEndpointHandler;

@AutoService(ServerModuleConfiguration.class)
public class TestServerModule implements ServerModuleConfiguration{
    @Override
    public void registerHandlers(HandlerRegistry registry) {
        registry.registerHandler(FirstRequest.class.getCanonicalName()+"_xyz", new FirstHandler());
        registry.registerHandler(SecondRequest.class.getCanonicalName(), new SecondHandler());
        registry.registerHandler(ThirdRequest.class.getCanonicalName(), new ThirdHandler());
    }

    @Override
    public void registerEndpoints(EndpointsRegistry registry) {
        registry.registerEndpoint("somePath", new EndpointsRegistry.EndpointHandlerFactory() {
            @Override
            public FirstHandlerEndpointHandler get() {
                return new FirstHandlerEndpointHandler();
            }
        });

        registry.registerEndpoint("somePath2", new EndpointsRegistry.EndpointHandlerFactory() {
            @Override
            public SecondHandlerEndpointHandler get() {
                return new SecondHandlerEndpointHandler();
            }
        });

        registry.registerEndpoint("somePath3", new EndpointsRegistry.EndpointHandlerFactory() {
            @Override
            public ThirdHandlerEndpointHandler get() {
                return new ThirdHandlerEndpointHandler();
            }
        });
    }
}
