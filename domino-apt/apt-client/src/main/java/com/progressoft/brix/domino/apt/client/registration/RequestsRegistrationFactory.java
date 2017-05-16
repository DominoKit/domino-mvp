package com.progressoft.brix.domino.apt.client.registration;

import com.progressoft.brix.domino.api.client.annotations.Request;
import com.progressoft.brix.domino.apt.client.ElementRegistration;
import com.progressoft.brix.domino.apt.client.RegistrationHelper;
import com.progressoft.brix.domino.apt.client.TwoArgumentsRegistrationFactory;
import com.progressoft.brix.domino.apt.client.request.RequestRegistration;
import com.progressoft.brix.domino.apt.client.request.RequestRegistrationImplementation;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public class RequestsRegistrationFactory extends TwoArgumentsRegistrationFactory {
    public RequestsRegistrationFactory(RegistrationHelper helper) {
        super(helper);
    }

    @Override
    protected ElementRegistration typeRegistration() {
        return new RequestRegistration(new RequestRegistrationImplementation(items()));
    }

    @Override
    protected Element targetType(Element e) {
        TypeMirror typeMirror = ((TypeElement) e).getSuperclass();
        TypeMirror generic = ((DeclaredType) typeMirror).getTypeArguments().get(0);
        return ((DeclaredType) generic).asElement();
    }

    @Override
    protected String annotation() {
        return Request.class.getCanonicalName();
    }
}
