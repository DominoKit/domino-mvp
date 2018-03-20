package org.dominokit.domino.apt.client;

import javax.annotation.Generated;
import org.dominokit.domino.api.client.ModuleConfiguration;
import org.dominokit.domino.api.client.request.LazyRequestRestSenderLoader;
import org.dominokit.domino.api.client.request.RequestRestSender;
import org.dominokit.domino.api.client.request.RequestRestSendersRegistry;

/**
 * This is generated class, please don't modify
 */
@Generated("org.dominokit.domino.apt.client.processors.module.client.ClientModuleAnnotationProcessor")
public class RequestSendersRegistrationsModuleConfiguration implements ModuleConfiguration {

    @Override
    public void registerRequestRestSenders(RequestRestSendersRegistry registry) {
        registry.registerRequestRestSender(AnnotatedClassWithHandlerPath.class.getCanonicalName(), new LazyRequestRestSenderLoader() {
            @Override
            protected RequestRestSender make() {
                return new AnnotatedClassWithRequestSender();
            }
        });
    }
}