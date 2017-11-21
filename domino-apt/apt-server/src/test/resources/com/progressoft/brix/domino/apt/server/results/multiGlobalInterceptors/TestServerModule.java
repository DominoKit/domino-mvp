package com.progressoft.brix.domino.apt.server.multiGlobalInterceptors;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.server.config.ServerModuleConfiguration;
import com.progressoft.brix.domino.api.server.interceptor.InterceptorsRegistry;
import com.progressoft.brix.domino.apt.server.FirstGlobalInterceptor;
import com.progressoft.brix.domino.apt.server.SecondGlobalInterceptor;
import com.progressoft.brix.domino.apt.server.TestServerEntryPointContext;
import com.progressoft.brix.domino.apt.server.ThirdGlobalInterceptor;
import javax.annotation.Generated;

@Generated("com.progressoft.brix.domino.apt.server.ServerModuleAnnotationProcessor")
@AutoService(ServerModuleConfiguration.class)
public class TestServerModule implements ServerModuleConfiguration{

    @Override
    public void registerGlobalInterceptors(InterceptorsRegistry registry) {
        registry.registerGlobalInterceptor(TestServerEntryPointContext.class.getCanonicalName(), new FirstGlobalInterceptor());
	    registry.registerGlobalInterceptor(TestServerEntryPointContext.class.getCanonicalName(), new SecondGlobalInterceptor());
	    registry.registerGlobalInterceptor(TestServerEntryPointContext.class.getCanonicalName(), new ThirdGlobalInterceptor());
    }
}
