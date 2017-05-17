package com.progressoft.brix.domino.apt.server.multiMix;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.server.config.ServerModuleConfiguration;
import com.progressoft.brix.domino.api.server.handler.HandlerRegistry;
import com.progressoft.brix.domino.apt.server.multiMix.FirstHandler;
import com.progressoft.brix.domino.apt.server.multiMix.FirstRequest;
import com.progressoft.brix.domino.apt.server.multiMix.SecondHandler;
import com.progressoft.brix.domino.apt.server.multiMix.SecondRequest;
import com.progressoft.brix.domino.apt.server.multiMix.ThirdHandler;
import com.progressoft.brix.domino.apt.server.multiMix.ThirdRequest;
import com.progressoft.brix.domino.api.server.endpoint.EndpointsRegistry;
import com.progressoft.brix.domino.apt.server.multiMix.FirstHandlerEndpointHandler;
import com.progressoft.brix.domino.apt.server.multiMix.SecondHandlerEndpointHandler;
import com.progressoft.brix.domino.apt.server.multiMix.ThirdHandlerEndpointHandler;
import com.progressoft.brix.domino.api.server.interceptor.InterceptorsRegistry;
import com.progressoft.brix.domino.apt.server.multiMix.FirstInterceptor;
import com.progressoft.brix.domino.apt.server.multiMix.TestServerEntryPointContext;
import com.progressoft.brix.domino.apt.server.multiMix.SecondInterceptor;
import com.progressoft.brix.domino.apt.server.multiMix.ThirdInterceptor;
import com.progressoft.brix.domino.apt.server.multiMix.FirstGlobalInterceptor;
import com.progressoft.brix.domino.apt.server.multiMix.SecondGlobalInterceptor;
import com.progressoft.brix.domino.apt.server.multiMix.ThirdGlobalInterceptor;

@AutoService(ServerModuleConfiguration.class)
public class TestServerModule implements ServerModuleConfiguration{

    @Override
    public void registerHandlers(HandlerRegistry registry) {
        registry.registerHandler(FirstRequest.class.getCanonicalName(), new FirstHandler());
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

    @Override
    public void registerInterceptors(InterceptorsRegistry registry) {
        registry.registerInterceptor(FirstRequest.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new FirstInterceptor());
        registry.registerInterceptor(SecondRequest.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new SecondInterceptor());
        registry.registerInterceptor(ThirdRequest.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new ThirdInterceptor());
    }

    @Override
    public void registerGlobalInterceptors(InterceptorsRegistry registry) {
        registry.registerGlobalInterceptor(TestServerEntryPointContext.class.getCanonicalName(), new FirstGlobalInterceptor());
        registry.registerGlobalInterceptor(TestServerEntryPointContext.class.getCanonicalName(), new SecondGlobalInterceptor());
        registry.registerGlobalInterceptor(TestServerEntryPointContext.class.getCanonicalName(), new ThirdGlobalInterceptor());
    }
}
