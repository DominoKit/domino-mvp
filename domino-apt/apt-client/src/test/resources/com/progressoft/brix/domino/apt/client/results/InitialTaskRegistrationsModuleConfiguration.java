package com.progressoft.brix.domino.apt.client;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.InitialTaskRegistry;
import com.progressoft.brix.domino.apt.client.AnnotatedClassWithInitialTask;

@AutoService(ModuleConfiguration.class)
public class InitialTaskRegistrationsModuleConfiguration implements ModuleConfiguration {

    @Override
    public void registerInitialTasks(InitialTaskRegistry registry) {
        registry.registerInitialTask(new AnnotatedClassWithInitialTask());
    }
}