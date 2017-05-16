package com.progressoft.brix.domino.apt.client.request;


import com.progressoft.brix.domino.api.client.request.RequestRestSendersRegistry;
import com.progressoft.brix.domino.apt.client.BaseElementRegistration;
import com.progressoft.brix.domino.apt.client.RegistrationImplementation;

public class RequestSenderRegistration extends BaseElementRegistration {

    public RequestSenderRegistration(RegistrationImplementation implementation) {
        super(implementation);
    }

    @Override
    protected Class<?> argumentType() {
        return RequestRestSendersRegistry.class;
    }

    @Override
    protected String methodName() {
        return "registerRequestRestSenders";
    }
}
