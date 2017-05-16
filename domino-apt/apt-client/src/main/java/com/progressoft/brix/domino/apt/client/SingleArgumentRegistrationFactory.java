package com.progressoft.brix.domino.apt.client;

import javax.lang.model.element.Element;
import java.util.List;

public abstract class SingleArgumentRegistrationFactory implements RegistrationFactory {

    protected RegistrationHelper helper;

    public SingleArgumentRegistrationFactory(RegistrationHelper helper) {
        this.helper = helper;
    }

    @Override
    public ElementRegistration registration() {
        if (!helper.isThereItems(annotation()))
            return NullElementRegistration.NULL_REGISTRATION;

        return typeRegistration();
    }

    protected List<Element> elements() {
        return helper.elementsAnnotatedWith(annotation());
    }

    protected abstract ElementRegistration typeRegistration();

    protected abstract boolean isValid(Element e);

    protected abstract String annotation();


}
