package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.InitialTaskRegistry;
import com.progressoft.brix.domino.apt.client.AnnotatedClassWithInitialTask;

public class InitialTaskRegistrationsModuleConfiguration implements ModuleConfiguration {

    @Override
    public void registerInitialTasks(InitialTaskRegistry registry) {
        registry.registerInitialTask(new AnnotatedClassWithInitialTask());
    }
}