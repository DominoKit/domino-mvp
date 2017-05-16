package com.progressoft.brix.domino.apt.client.registration;

import com.progressoft.brix.domino.api.client.InitializeTask;
import com.progressoft.brix.domino.api.client.annotations.InitialTask;
import com.progressoft.brix.domino.apt.client.ElementRegistration;
import com.progressoft.brix.domino.apt.client.RegistrationHelper;
import com.progressoft.brix.domino.apt.client.SingleArgumentRegistrationFactory;
import com.progressoft.brix.domino.apt.client.initialtask.InitialTasksRegistration;
import com.progressoft.brix.domino.apt.client.initialtask.InitialTasksRegistrationImplementation;

import javax.lang.model.element.Element;

public class InitialTasksRegistrationFactory extends SingleArgumentRegistrationFactory {

    public InitialTasksRegistrationFactory(RegistrationHelper helper) {
        super(helper);
    }

    @Override
    protected ElementRegistration typeRegistration() {
        return new InitialTasksRegistration(new InitialTasksRegistrationImplementation(elements()));
    }

    @Override
    protected boolean isValid(Element e) {
        return helper.isImplementsGenericInterface(e, InitializeTask.class);
    }

    @Override
    protected String annotation() {
        return InitialTask.class.getCanonicalName();
    }
}
