package org.dominokit.domino.apt.server.multiMix;

import com.google.auto.service.AutoService;
import java.util.function.Supplier;
import javax.annotation.Generated;
import org.dominokit.domino.api.server.config.ServerModuleConfiguration;
import org.dominokit.domino.api.server.endpoint.EndpointsRegistry;
import org.dominokit.domino.api.server.handler.HandlerRegistry;
import org.dominokit.domino.api.server.interceptor.InterceptorsRegistry;
import org.dominokit.domino.apt.server.FirstGlobalInterceptor;
import org.dominokit.domino.apt.server.FirstHandler;
import org.dominokit.domino.apt.server.FirstHandlerEndpointHandler;
import org.dominokit.domino.apt.server.FirstInterceptor;
import org.dominokit.domino.apt.server.SecondGlobalInterceptor;
import org.dominokit.domino.apt.server.SecondHandler;
import org.dominokit.domino.apt.server.SecondHandlerEndpointHandler;
import org.dominokit.domino.apt.server.SecondInterceptor;
import org.dominokit.domino.apt.server.TestServerEntryPointContext;
import org.dominokit.domino.apt.server.ThirdGlobalInterceptor;
import org.dominokit.domino.apt.server.ThirdHandler;
import org.dominokit.domino.apt.server.ThirdHandlerEndpointHandler;
import org.dominokit.domino.apt.server.ThirdInterceptor;

/**
 * This is generated class, please don't modify
 */
@Generated("org.dominokit.domino.apt.server.ServerModuleAnnotationProcessor")
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

    @Override
    public void registerInterceptors(InterceptorsRegistry registry) {
        registry.registerInterceptor(FirstHandler.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new FirstInterceptor());
        registry.registerInterceptor(SecondHandler.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new SecondInterceptor());
        registry.registerInterceptor(ThirdHandler.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new ThirdInterceptor());
    }

    @Override
    public void registerGlobalInterceptors(InterceptorsRegistry registry) {
        registry.registerGlobalInterceptor(TestServerEntryPointContext.class.getCanonicalName(), new FirstGlobalInterceptor());
        registry.registerGlobalInterceptor(TestServerEntryPointContext.class.getCanonicalName(), new SecondGlobalInterceptor());
        registry.registerGlobalInterceptor(TestServerEntryPointContext.class.getCanonicalName(), new ThirdGlobalInterceptor());
    }
}
