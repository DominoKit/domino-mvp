package com.progressoft.brix.domino.apt.server.singleGlobalInterceptor;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.server.ServerModuleConfiguration;
import com.progressoft.brix.domino.api.server.InterceptorsRegistry;
import com.progressoft.brix.domino.apt.server.singleGlobalInterceptor.FirstGlobalInterceptor;
import com.progressoft.brix.domino.apt.server.singleGlobalInterceptor.TestServerEntryPointContext;

@AutoService(ServerModuleConfiguration.class)
public class TestServerModule implements ServerModuleConfiguration{

    @Override
    public void registerGlobalInterceptors(InterceptorsRegistry registry) {
        registry.registerGlobalInterceptor(TestServerEntryPointContext.class.getCanonicalName(), new FirstGlobalInterceptor());
    }
}
