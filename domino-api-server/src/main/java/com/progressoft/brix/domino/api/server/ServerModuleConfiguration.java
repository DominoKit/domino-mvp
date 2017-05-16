package com.progressoft.brix.domino.api.server;

public interface ServerModuleConfiguration {
    default void registerHandlers(HandlerRegistry registry){}
    default void registerInterceptors(InterceptorsRegistry registry){}
    default void registerGlobalInterceptors(InterceptorsRegistry registry){}
    default void registerEndpoints(EndpointsRegistry registry){}
}