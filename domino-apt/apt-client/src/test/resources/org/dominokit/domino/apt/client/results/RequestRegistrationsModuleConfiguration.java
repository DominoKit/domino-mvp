package org.dominokit.domino.apt.client;

import javax.annotation.Generated;
import org.dominokit.domino.api.client.ModuleConfiguration;
import org.dominokit.domino.api.client.request.CommandRegistry;

/**
 * This is generated class, please don't modify
 */
@Generated("org.dominokit.domino.apt.client.processors.module.client.ClientModuleAnnotationProcessor")
public class RequestRegistrationsModuleConfiguration implements ModuleConfiguration {
    @Override
    public void registerRequests(CommandRegistry registry) {
        registry.registerCommand(AnnotatedClassWithRequest.class.getCanonicalName(),
                DefaultAnnotatedClassWithPresenter.class.getCanonicalName());
    }
}