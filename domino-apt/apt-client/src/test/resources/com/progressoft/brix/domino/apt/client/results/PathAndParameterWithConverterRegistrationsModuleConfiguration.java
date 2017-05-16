package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.history.PathToRequestMapperRegistry;
import com.progressoft.brix.domino.api.client.history.TokenizedPath;
import com.progressoft.brix.domino.apt.client.AnnotatedClassWithPathAndParameterWithConverter;
import com.progressoft.brix.domino.api.client.history.RequestFromPath;
import com.progressoft.brix.domino.api.client.request.Request;
import com.progressoft.brix.domino.apt.client.BigDecimalConverter;

public class PathAndParameterWithConverterRegistrationsModuleConfiguration implements ModuleConfiguration {

    @Override
    public void registerPathMappers(PathToRequestMapperRegistry registry) {
        registry.registerMapper("somePath", new RequestFromPath() {
            @Override
            public Request buildRequest(TokenizedPath path) {
                return new AnnotatedClassWithPathAndParameterWithConverter(
                        new BigDecimalConverter().convert(path.getParameter("value")));
            }
        });
    }
}