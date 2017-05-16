package com.progressoft.brix.domino.apt.client.contribution;

import com.progressoft.brix.domino.api.client.extension.ContributionsRegistry;
import com.progressoft.brix.domino.apt.client.BaseElementRegistration;
import com.progressoft.brix.domino.apt.client.RegistrationImplementation;

public class ContributionRegistration extends BaseElementRegistration {
    public ContributionRegistration(RegistrationImplementation implementation) {
        super(implementation);
    }

    @Override
    protected Class<?> argumentType() {
        return ContributionsRegistry.class;
    }

    @Override
    protected String methodName() {
        return "registerContributions";
    }
}
