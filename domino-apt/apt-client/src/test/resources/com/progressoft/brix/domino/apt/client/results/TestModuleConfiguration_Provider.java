package com.progressoft.brix.domino.apt.client;

import com.google.auto.service.AutoService;
import com.google.common.annotations.GwtIncompatible;
import com.progressoft.brix.domino.api.client.ConfigurationProvider;
import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import javax.annotation.Generated;

@Generated("com.progressoft.brix.domino.apt.client.processors.module.client.ConfigurationProviderAnnotationProcessor")
@GwtIncompatible("Unused in GWT compilation")
@AutoService(ConfigurationProvider.class)
public class TestModuleConfiguration_Provider implements ConfigurationProvider {

    @Override
    public ModuleConfiguration get(){
        return new TestModuleConfiguration();
    }
}