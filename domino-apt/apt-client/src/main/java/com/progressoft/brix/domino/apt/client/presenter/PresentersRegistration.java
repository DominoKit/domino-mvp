package com.progressoft.brix.domino.apt.client.presenter;

import com.progressoft.brix.domino.api.client.mvp.PresenterRegistry;
import com.progressoft.brix.domino.apt.client.BaseElementRegistration;
import com.progressoft.brix.domino.apt.client.RegistrationImplementation;

public class PresentersRegistration extends BaseElementRegistration {

    public PresentersRegistration(RegistrationImplementation implementation) {
        super(implementation);
    }

    @Override
    protected Class<?> argumentType() {
        return PresenterRegistry.class;
    }

    @Override
    protected String methodName() {
        return "registerPresenters";
    }
}
