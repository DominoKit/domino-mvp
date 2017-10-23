package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.request.RequestRegistry;
import java.lang.Override;
import javax.annotation.Generated;

@Generated("com.progressoft.brix.domino.apt.client.processors.module.client.ClientModuleAnnotationProcessor")
public class RequestRegistrationsModuleConfiguration implements ModuleConfiguration {
    @Override
    public void registerRequests(RequestRegistry registry) {
        registry.registerRequest(AnnotatedClassWithRequest.class.getCanonicalName(),
                PresenterInterface.class.getCanonicalName());
    }
}