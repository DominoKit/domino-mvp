package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.history.PathToRequestMapperRegistry;
import com.progressoft.brix.domino.api.client.history.RequestFromPath;
import com.progressoft.brix.domino.api.client.history.TokenizedPath;
import com.progressoft.brix.domino.api.client.request.Request;
import com.progressoft.brix.domino.apt.client.AnnotatedClassWithPathAndCustomMapper;
import com.progressoft.brix.domino.apt.client.SampleMapper;

public class PathAndCustomMapperRegistrationsModuleConfiguration implements ModuleConfiguration {

    @Override
    public void registerPathMappers(PathToRequestMapperRegistry registry) {
        registry.registerMapper("somePath", new SampleMapper());
    }
}