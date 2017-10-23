package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.InitialTaskRegistry;
import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import java.lang.Override;
import javax.annotation.Generated;

@Generated("com.progressoft.brix.domino.apt.client.processors.module.client.ClientModuleAnnotationProcessor")
public class InitialTaskRegistrationsModuleConfiguration implements ModuleConfiguration {

    @Override
    public void registerInitialTasks(InitialTaskRegistry registry) {
        registry.registerInitialTask(new AnnotatedClassWithInitialTask());
    }
}