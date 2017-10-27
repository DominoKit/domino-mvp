package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.extension.ContributionsRegistry;
import com.progressoft.brix.domino.api.shared.extension.MainExtensionPoint;
import javax.annotation.Generated;

@Generated("com.progressoft.brix.domino.apt.client.processors.module.client.ClientModuleAnnotationProcessor")
public class ContributionRegistrationsModuleConfiguration implements ModuleConfiguration {

    @Override
    public void registerContributions(ContributionsRegistry registry) {
        registry.registerContribution(MainExtensionPoint.class, new AnnotatedClassWithContribution());
    }
}