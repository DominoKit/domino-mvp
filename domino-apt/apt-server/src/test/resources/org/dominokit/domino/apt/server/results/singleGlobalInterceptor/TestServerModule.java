package org.dominokit.domino.apt.server.singleGlobalInterceptor;

import com.google.auto.service.AutoService;
import javax.annotation.Generated;
import org.dominokit.domino.api.server.config.ServerModuleConfiguration;
import org.dominokit.domino.api.server.interceptor.InterceptorsRegistry;
import org.dominokit.domino.apt.server.FirstGlobalInterceptor;
import org.dominokit.domino.apt.server.TestServerEntryPointContext;

/**
 * This is generated class, please don't modify
 */
@Generated("org.dominokit.domino.apt.server.ServerModuleAnnotationProcessor")
@AutoService(ServerModuleConfiguration.class)
public class TestServerModule implements ServerModuleConfiguration{

    @Override
    public void registerGlobalInterceptors(InterceptorsRegistry registry) {
        registry.registerGlobalInterceptor(TestServerEntryPointContext.class.getCanonicalName(), new FirstGlobalInterceptor());
    }
}
