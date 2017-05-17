package com.progressoft.brix.domino.apt.server.multiInterceptors;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.server.config.ServerModuleConfiguration;
import com.progressoft.brix.domino.api.server.interceptor.InterceptorsRegistry;
import com.progressoft.brix.domino.apt.server.multiInterceptors.FirstInterceptor;
import com.progressoft.brix.domino.apt.server.multiInterceptors.FirstRequest;
import com.progressoft.brix.domino.apt.server.multiInterceptors.TestServerEntryPointContext;
import com.progressoft.brix.domino.apt.server.multiInterceptors.SecondInterceptor;
import com.progressoft.brix.domino.apt.server.multiInterceptors.SecondRequest;
import com.progressoft.brix.domino.apt.server.multiInterceptors.ThirdInterceptor;
import com.progressoft.brix.domino.apt.server.multiInterceptors.ThirdRequest;

@AutoService(ServerModuleConfiguration.class)
public class TestServerModule implements ServerModuleConfiguration{

    @Override
    public void registerInterceptors(InterceptorsRegistry registry) {
        registry.registerInterceptor(FirstRequest.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new FirstInterceptor());
        registry.registerInterceptor(SecondRequest.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new SecondInterceptor());
        registry.registerInterceptor(ThirdRequest.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new ThirdInterceptor());
    }
}
