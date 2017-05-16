package com.progressoft.brix.domino.apt.server.multiGlobalInterceptors;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.server.ServerModuleConfiguration;
import com.progressoft.brix.domino.api.server.InterceptorsRegistry;
import com.progressoft.brix.domino.apt.server.multiGlobalInterceptors.FirstGlobalInterceptor;
import com.progressoft.brix.domino.apt.server.multiGlobalInterceptors.TestServerEntryPointContext;
import com.progressoft.brix.domino.apt.server.multiGlobalInterceptors.SecondGlobalInterceptor;
import com.progressoft.brix.domino.apt.server.multiGlobalInterceptors.ThirdGlobalInterceptor;

@AutoService(ServerModuleConfiguration.class)
public class TestServerModule implements ServerModuleConfiguration{

    @Override
    public void registerGlobalInterceptors(InterceptorsRegistry registry) {
        registry.registerGlobalInterceptor(TestServerEntryPointContext.class.getCanonicalName(), new FirstGlobalInterceptor());
	    registry.registerGlobalInterceptor(TestServerEntryPointContext.class.getCanonicalName(), new SecondGlobalInterceptor());
	    registry.registerGlobalInterceptor(TestServerEntryPointContext.class.getCanonicalName(), new ThirdGlobalInterceptor());
    }
}
