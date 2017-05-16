package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.history.PathToRequestMapperRegistry;
import com.progressoft.brix.domino.apt.client.SampleMapper;

public class PathAndCustomMapperRegistrationsModuleConfiguration implements ModuleConfiguration {

    @Override
    public void registerPathMappers(PathToRequestMapperRegistry registry) {
        registry.registerMapper("somePath", new SampleMapper());
    }
}