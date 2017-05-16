package com.progressoft.brix.domino.apt.server.singleInterceptor;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.server.ServerModuleConfiguration;
import com.progressoft.brix.domino.api.server.InterceptorsRegistry;
import com.progressoft.brix.domino.apt.server.singleInterceptor.FirstInterceptor;
import com.progressoft.brix.domino.apt.server.singleInterceptor.FirstRequest;
import com.progressoft.brix.domino.apt.server.singleInterceptor.TestServerEntryPointContext;

@AutoService(ServerModuleConfiguration.class)
public class TestServerModule implements ServerModuleConfiguration{

    @Override
    public void registerInterceptors(InterceptorsRegistry registry) {
        registry.registerInterceptor(FirstRequest.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new FirstInterceptor());
    }
}
