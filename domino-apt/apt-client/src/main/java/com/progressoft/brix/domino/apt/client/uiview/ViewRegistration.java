package com.progressoft.brix.domino.apt.client.uiview;

import com.progressoft.brix.domino.api.client.mvp.ViewRegistry;
import com.progressoft.brix.domino.apt.client.BaseElementRegistration;
import com.progressoft.brix.domino.apt.client.RegistrationImplementation;

public class ViewRegistration extends BaseElementRegistration {

    public ViewRegistration(RegistrationImplementation implementation) {
        super(implementation);
    }

    @Override
    protected Class<?> argumentType() {
        return ViewRegistry.class;
    }

    @Override
    protected String methodName() {
        return "registerViews";
    }
}

