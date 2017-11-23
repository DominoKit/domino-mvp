package com.progressoft.brix.domino.apt.server.multiInterceptors;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.server.config.ServerModuleConfiguration;
import com.progressoft.brix.domino.api.server.interceptor.InterceptorsRegistry;
import com.progressoft.brix.domino.apt.server.FirstHandler;
import com.progressoft.brix.domino.apt.server.FirstInterceptor;
import com.progressoft.brix.domino.apt.server.SecondHandler;
import com.progressoft.brix.domino.apt.server.SecondInterceptor;
import com.progressoft.brix.domino.apt.server.TestServerEntryPointContext;
import com.progressoft.brix.domino.apt.server.ThirdHandler;
import com.progressoft.brix.domino.apt.server.ThirdInterceptor;
import javax.annotation.Generated;

@Generated("com.progressoft.brix.domino.apt.server.ServerModuleAnnotationProcessor")
@AutoService(ServerModuleConfiguration.class)
public class TestServerModule implements ServerModuleConfiguration {

    @Override
    public void registerInterceptors(InterceptorsRegistry registry) {
        registry.registerInterceptor(FirstHandler.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new FirstInterceptor());
        registry.registerInterceptor(SecondHandler.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new SecondInterceptor());
        registry.registerInterceptor(ThirdHandler.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new ThirdInterceptor());
    }
}
