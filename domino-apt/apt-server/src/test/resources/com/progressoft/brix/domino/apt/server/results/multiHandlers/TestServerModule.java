package com.progressoft.brix.domino.apt.server.multiHandlers;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.server.config.ServerModuleConfiguration;
import com.progressoft.brix.domino.api.server.endpoint.EndpointsRegistry;
import com.progressoft.brix.domino.api.server.handler.HandlerRegistry;
import com.progressoft.brix.domino.apt.server.FirstHandler;
import com.progressoft.brix.domino.apt.server.FirstHandlerEndpointHandler;
import com.progressoft.brix.domino.apt.server.SecondHandler;
import com.progressoft.brix.domino.apt.server.SecondHandlerEndpointHandler;
import com.progressoft.brix.domino.apt.server.ThirdHandler;
import com.progressoft.brix.domino.apt.server.ThirdHandlerEndpointHandler;
import java.util.function.Supplier;
import javax.annotation.Generated;

@Generated("com.progressoft.brix.domino.apt.server.ServerModuleAnnotationProcessor")
@AutoService(ServerModuleConfiguration.class)
public class TestServerModule implements ServerModuleConfiguration {
    @Override
    public void registerHandlers(HandlerRegistry registry) {
        registry.registerHandler("somePath", new FirstHandler());
        registry.registerHandler("somePath2", new SecondHandler());
        registry.registerHandler("somePath3", new ThirdHandler());
    }

    @Override
    public void registerEndpoints(EndpointsRegistry registry) {
        registry.registerEndpoint("somePath", new Supplier<FirstHandlerEndpointHandler>() {
            @Override
            public FirstHandlerEndpointHandler get() {
                return new FirstHandlerEndpointHandler();
            }
        });
        registry.registerEndpoint("somePath2", new Supplier<SecondHandlerEndpointHandler>() {
            @Override
            public SecondHandlerEndpointHandler get() {
                return new SecondHandlerEndpointHandler();
            }
        });
        registry.registerEndpoint("somePath3", new Supplier<ThirdHandlerEndpointHandler>() {
            @Override
            public ThirdHandlerEndpointHandler get() {
                return new ThirdHandlerEndpointHandler();
            }
        });
    }
}
