package com.progressoft.brix.domino.apt.client;

import com.google.auto.service.AutoService;
import com.google.gwt.core.shared.GwtIncompatible;
import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.ModuleConfigurationProvider;

import javax.annotation.Generated;

@Generated("com.progressoft.brix.domino.apt.client.processors.module.client.ConfigurationProviderAnnotationProcessor")
@GwtIncompatible("Unused in GWT compilation")
@AutoService(ModuleConfigurationProvider.class)
public class TestModuleConfiguration_Provider implements ModuleConfigurationProvider {

    @Override
    public ModuleConfiguration get(){
        return new TestModuleConfiguration();
    }
}