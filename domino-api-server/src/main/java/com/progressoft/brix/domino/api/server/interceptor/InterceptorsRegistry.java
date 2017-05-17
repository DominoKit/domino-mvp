package com.progressoft.brix.domino.api.server.interceptor;

public interface InterceptorsRegistry {
    void registerInterceptor(String requestName, String entryPointName, RequestInterceptor interceptor);
    void registerGlobalInterceptor(String entryPointName, GlobalRequestInterceptor interceptor);
}
