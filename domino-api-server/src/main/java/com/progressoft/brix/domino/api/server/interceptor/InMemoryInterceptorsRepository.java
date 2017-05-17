package com.progressoft.brix.domino.api.server.interceptor;

import java.util.*;

public class InMemoryInterceptorsRepository implements InterceptorsRepository {

    private final Map<String, Map<String, Set<RequestInterceptor>>> interceptors = new HashMap<>();

    private final Map<String, Set<GlobalRequestInterceptor>> globalInterceptors=new HashMap<>();

    @Override
    public void addInterceptor(String requestName, String entryPointName, RequestInterceptor interceptor) {
        if (interceptors.containsKey(requestName))
            interceptors.get(requestName).get(entryPointName).add(interceptor);
        else {
            Map<String, Set<RequestInterceptor>> items = new HashMap<>();
            Set<RequestInterceptor> requestInterceptors=new HashSet<>();
            requestInterceptors.add(interceptor);
            items.put(entryPointName, requestInterceptors);
            interceptors.put(requestName, items);
        }
    }

    @Override
    public Collection<RequestInterceptor> findInterceptors(String requestName, String entryPointName) {
        if(interceptors.containsKey(requestName))
            return interceptors.get(requestName).get(entryPointName);
        return new HashSet<>();
    }

    @Override
    public void addGlobalInterceptor(String entryPointName, GlobalRequestInterceptor interceptor) {
        if(globalInterceptors.containsKey(entryPointName))
            globalInterceptors.get(entryPointName).add(interceptor);
        else {
            Set<GlobalRequestInterceptor> requestGlobalInterceptor = new HashSet<>();
            requestGlobalInterceptor.add(interceptor);
            globalInterceptors.put(entryPointName, requestGlobalInterceptor);
        }
    }

    @Override
    public Collection<GlobalRequestInterceptor> findGlobalInterceptors(String entryPointName) {
        if(globalInterceptors.containsKey(entryPointName))
            return globalInterceptors.get(entryPointName);
        return new HashSet<>();
    }
}
