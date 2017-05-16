package com.progressoft.brix.domino.apt.client.request;

import com.progressoft.brix.domino.api.client.request.RequestRegistry;
import com.progressoft.brix.domino.apt.client.BaseElementRegistration;
import com.progressoft.brix.domino.apt.client.RegistrationImplementation;

public class RequestRegistration extends BaseElementRegistration {

    public RequestRegistration(RegistrationImplementation implementation) {
        super(implementation);
    }

    @Override
    protected Class<?> argumentType() {
        return RequestRegistry.class;
    }

    @Override
    protected String methodName() {
        return "registerRequests";
    }
}
