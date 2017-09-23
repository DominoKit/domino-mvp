package com.progressoft.brix.domino.apt.client;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.request.RequestRestSendersRegistry;
import com.progressoft.brix.domino.api.client.request.LazyRequestRestSenderLoader;
import com.progressoft.brix.domino.api.client.request.RequestRestSender;
import com.progressoft.brix.domino.apt.client.AnnotatedClassWithHandlerPath;
import com.progressoft.brix.domino.apt.client.AnnotatedClassWithRequestSender;

@AutoService(ModuleConfiguration.class)
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