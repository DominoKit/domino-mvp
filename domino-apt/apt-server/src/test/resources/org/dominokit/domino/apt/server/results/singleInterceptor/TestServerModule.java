package org.dominokit.domino.apt.server.singleInterceptor;

import com.google.auto.service.AutoService;
import javax.annotation.Generated;
import org.dominokit.domino.api.server.config.ServerModuleConfiguration;
import org.dominokit.domino.api.server.interceptor.InterceptorsRegistry;
import org.dominokit.domino.apt.server.FirstHandler;
import org.dominokit.domino.apt.server.FirstInterceptor;
import org.dominokit.domino.apt.server.TestServerEntryPointContext;

/**
 * This is generated class, please don't modify
 */
@Generated("org.dominokit.domino.apt.server.ServerModuleAnnotationProcessor")
@AutoService(ServerModuleConfiguration.class)
public class TestServerModule implements ServerModuleConfiguration {

    @Override
    public void registerInterceptors(InterceptorsRegistry registry) {
        registry.registerInterceptor(FirstHandler.class.getCanonicalName(), TestServerEntryPointContext.class.getCanonicalName(), new FirstInterceptor());
    }
}
