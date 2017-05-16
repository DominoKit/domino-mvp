package com.progressoft.brix.domino.apt.client.registration;

import com.progressoft.brix.domino.api.client.annotations.Contribute;
import com.progressoft.brix.domino.apt.client.ElementRegistration;
import com.progressoft.brix.domino.apt.client.RegistrationHelper;
import com.progressoft.brix.domino.apt.client.TwoArgumentsRegistrationFactory;
import com.progressoft.brix.domino.apt.client.contribution.ContributionRegistration;
import com.progressoft.brix.domino.apt.client.contribution.ContributionRegistrationImplementation;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public class ContributionRegistrationFactory extends TwoArgumentsRegistrationFactory {
    public ContributionRegistrationFactory(RegistrationHelper helper) {
        super(helper);
    }

    @Override
    protected ElementRegistration typeRegistration() {
        return new ContributionRegistration(new ContributionRegistrationImplementation(items()));
    }

    @Override
    protected Element targetType(Element e) {
        TypeMirror typeMirror = ((TypeElement) e).getInterfaces().get(0);
        TypeMirror generic = ((DeclaredType) typeMirror).getTypeArguments().get(0);
        return ((DeclaredType) generic).asElement();
    }

    @Override
    protected String annotation() {
        return Contribute.class.getCanonicalName();
    }
}
