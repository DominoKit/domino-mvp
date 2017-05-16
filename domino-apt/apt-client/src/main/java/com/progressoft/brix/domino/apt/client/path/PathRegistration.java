package com.progressoft.brix.domino.apt.client.path;

import com.progressoft.brix.domino.api.client.history.PathToRequestMapperRegistry;
import com.progressoft.brix.domino.apt.client.BaseElementRegistration;
import com.progressoft.brix.domino.apt.client.RegistrationImplementation;

public class PathRegistration extends BaseElementRegistration {
    public PathRegistration(RegistrationImplementation implementation) {
        super(implementation);
    }

    @Override
    protected Class<?> argumentType() {
        return PathToRequestMapperRegistry.class;
    }

    @Override
    protected String methodName() {
        return "registerPathMappers";
    }
}
