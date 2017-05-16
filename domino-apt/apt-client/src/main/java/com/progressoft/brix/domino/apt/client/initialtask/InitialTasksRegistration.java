package com.progressoft.brix.domino.apt.client.initialtask;

import com.progressoft.brix.domino.api.client.InitialTaskRegistry;
import com.progressoft.brix.domino.apt.client.BaseElementRegistration;
import com.progressoft.brix.domino.apt.client.RegistrationImplementation;

public class InitialTasksRegistration extends BaseElementRegistration {

    public InitialTasksRegistration(RegistrationImplementation implementation) {
        super(implementation);
    }

    @Override
    protected Class<?> argumentType() {
        return InitialTaskRegistry.class;
    }

    @Override
    protected String methodName() {
        return "registerInitialTasks";
    }
}
