package org.dominokit.domino.apt.server.singleHandler;

import com.google.auto.service.AutoService;
import java.util.function.Supplier;
import javax.annotation.Generated;
import org.dominokit.domino.api.server.config.ServerModuleConfiguration;
import org.dominokit.domino.api.server.endpoint.EndpointsRegistry;
import org.dominokit.domino.api.server.handler.HandlerRegistry;
import org.dominokit.domino.apt.server.HandlerImplementingRequestHandlerInterface;
import org.dominokit.domino.apt.server.HandlerImplementingRequestHandlerInterfaceEndpointHandler;

/**
 * This is generated class, please don't modify
 */
@Generated("org.dominokit.domino.apt.server.ServerModuleAnnotationProcessor")
@AutoService(ServerModuleConfiguration.class)
public class TestServerModule implements ServerModuleConfiguration{

    @Override
    public void registerHandlers(HandlerRegistry registry) {
        registry.registerHandler("somePath", new HandlerImplementingRequestHandlerInterface());
    }

    @Override
    public void registerEndpoints(EndpointsRegistry registry) {
        registry.registerEndpoint("somePath", new Supplier<HandlerImplementingRequestHandlerInterfaceEndpointHandler>() {
            @Override
            public HandlerImplementingRequestHandlerInterfaceEndpointHandler get() {
                return new HandlerImplementingRequestHandlerInterfaceEndpointHandler();
            }
        });
    }
}