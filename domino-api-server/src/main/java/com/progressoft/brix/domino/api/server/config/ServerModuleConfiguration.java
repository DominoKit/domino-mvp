package com.progressoft.brix.domino.api.server.config;

import com.progressoft.brix.domino.api.server.endpoint.EndpointsRegistry;
import com.progressoft.brix.domino.api.server.handler.HandlerRegistry;
import com.progressoft.brix.domino.api.server.interceptor.InterceptorsRegistry;

public interface ServerModuleConfiguration {
    default void registerHandlers(HandlerRegistry registry){}
    default void registerInterceptors(InterceptorsRegistry registry){}
    default void registerGlobalInterceptors(InterceptorsRegistry registry){}
    default void registerEndpoints(EndpointsRegistry registry){}
}