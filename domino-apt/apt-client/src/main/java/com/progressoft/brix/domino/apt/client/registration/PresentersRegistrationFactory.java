package com.progressoft.brix.domino.apt.client.registration;

import com.progressoft.brix.domino.api.client.annotations.Presenter;
import com.progressoft.brix.domino.apt.client.ElementRegistration;
import com.progressoft.brix.domino.apt.client.RegistrationHelper;
import com.progressoft.brix.domino.apt.client.TwoArgumentsRegistrationFactory;
import com.progressoft.brix.domino.apt.client.presenter.PresenterRegistrationImplementation;
import com.progressoft.brix.domino.apt.client.presenter.PresentersRegistration;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

public class PresentersRegistrationFactory extends TwoArgumentsRegistrationFactory {

    public PresentersRegistrationFactory(RegistrationHelper helper) {
        super(helper);
    }

    @Override
    protected ElementRegistration typeRegistration() {
        return new PresentersRegistration(new PresenterRegistrationImplementation(items()));
    }

    @Override
    protected Element targetType(Element e) {
        return ((DeclaredType) ((TypeElement) e).getInterfaces().get(0)).asElement();
    }

    @Override
    protected String annotation() {
        return Presenter.class.getCanonicalName();
    }

}
