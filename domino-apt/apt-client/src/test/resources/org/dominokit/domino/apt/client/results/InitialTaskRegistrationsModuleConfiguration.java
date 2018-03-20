package org.dominokit.domino.apt.client;

import javax.annotation.Generated;
import org.dominokit.domino.api.client.InitialTaskRegistry;
import org.dominokit.domino.api.client.ModuleConfiguration;

@Generated("org.dominokit.domino.apt.client.processors.module.client.ClientModuleAnnotationProcessor")
public class InitialTaskRegistrationsModuleConfiguration implements ModuleConfiguration {

    @Override
    public void registerInitialTasks(InitialTaskRegistry registry) {
        registry.registerInitialTask(new AnnotatedClassWithInitialTask());
    }
}