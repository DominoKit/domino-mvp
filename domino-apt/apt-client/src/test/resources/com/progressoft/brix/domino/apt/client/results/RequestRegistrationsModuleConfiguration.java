package com.progressoft.brix.domino.apt.client;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.request.RequestRegistry;
import com.progressoft.brix.domino.apt.client.AnnotatedClassWithRequest;
import com.progressoft.brix.domino.apt.client.PresenterInterface;

@AutoService(ModuleConfiguration.class)
public class RequestRegistrationsModuleConfiguration implements ModuleConfiguration {
    @Override
    public void registerRequests(RequestRegistry registry) {
        registry.registerRequest(AnnotatedClassWithRequest.class.getCanonicalName(),
                PresenterInterface.class.getCanonicalName());
    }
}