package org.dominokit.domino.apt.client;

import javax.annotation.Generated;
import org.dominokit.domino.api.client.ModuleConfiguration;
import org.dominokit.domino.api.client.extension.ContributionsRegistry;
import org.dominokit.domino.api.shared.extension.MainExtensionPoint;

/**
 * This is generated class, please don't modify
 */
@Generated("org.dominokit.domino.apt.client.processors.module.client.ClientModuleAnnotationProcessor")
public class ContributionRegistrationsModuleConfiguration implements ModuleConfiguration {

    @Override
    public void registerContributions(ContributionsRegistry registry) {
        registry.registerContribution(MainExtensionPoint.class, new AnnotatedClassWithContribution());
    }
}