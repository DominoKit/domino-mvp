package com.progressoft.brix.domino.apt.client;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.client.ConfigurationProvider;
import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import java.lang.Override;

@AutoService(ConfigurationProvider.class)
public class TestModuleConfiguration_Provider implements ConfigurationProvider {

    @Override
    public ModuleConfiguration get(){
        return new TestModuleConfiguration();
    }
}