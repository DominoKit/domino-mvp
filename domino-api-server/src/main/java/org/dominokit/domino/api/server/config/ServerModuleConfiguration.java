package org.dominokit.domino.api.server.config;

import org.dominokit.domino.api.server.endpoint.EndpointsRegistry;
import org.dominokit.domino.api.server.handler.HandlerRegistry;
import org.dominokit.domino.api.server.interceptor.InterceptorsRegistry;

public interface ServerModuleConfiguration {
    default void registerHandlers(HandlerRegistry registry){/*dont force implementation*/}
    default void registerInterceptors(InterceptorsRegistry registry){/*dont force implementation*/}
    default void registerGlobalInterceptors(InterceptorsRegistry registry){/*dont force implementation*/}
    default void registerEndpoints(EndpointsRegistry registry){/*dont force implementation*/}
}