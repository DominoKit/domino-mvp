package com.progressoft.brix.domino.api.server.config;

import com.progressoft.brix.domino.api.server.endpoint.EndpointsRegistry;
import com.progressoft.brix.domino.api.server.handler.HandlerRegistry;
import com.progressoft.brix.domino.api.server.interceptor.InterceptorsRegistry;

public interface ServerModuleConfiguration {
    default void registerHandlers(HandlerRegistry registry){/*dont force implementation*/}
    default void registerInterceptors(InterceptorsRegistry registry){/*dont force implementation*/}
    default void registerGlobalInterceptors(InterceptorsRegistry registry){/*dont force implementation*/}
    default void registerEndpoints(EndpointsRegistry registry){/*dont force implementation*/}
}