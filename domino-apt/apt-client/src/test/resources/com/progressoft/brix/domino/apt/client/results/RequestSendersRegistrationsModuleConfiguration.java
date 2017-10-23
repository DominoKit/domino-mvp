package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.request.LazyRequestRestSenderLoader;
import com.progressoft.brix.domino.api.client.request.RequestRestSender;
import com.progressoft.brix.domino.api.client.request.RequestRestSendersRegistry;
import java.lang.Override;
import javax.annotation.Generated;

@Generated("com.progressoft.brix.domino.apt.client.processors.module.client.ClientModuleAnnotationProcessor")
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