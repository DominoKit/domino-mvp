package com.progressoft.brix.domino.apt.client.registration;

import com.google.auto.common.MoreElements;
import com.progressoft.brix.domino.api.client.annotations.RequestSender;
import com.progressoft.brix.domino.apt.client.ElementRegistration;
import com.progressoft.brix.domino.apt.client.RegistrationHelper;
import com.progressoft.brix.domino.apt.client.TwoArgumentsRegistrationFactory;
import com.progressoft.brix.domino.apt.client.request.RequestSenderRegistration;
import com.progressoft.brix.domino.apt.client.request.RequestSenderRegistrationImplementation;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import java.util.Map;

public class RequestSenderRegistrationFactory extends TwoArgumentsRegistrationFactory {

    public RequestSenderRegistrationFactory(RegistrationHelper helper) {
        super(helper);
    }

    @Override
    protected ElementRegistration typeRegistration() {
        return new RequestSenderRegistration(new RequestSenderRegistrationImplementation(items()));
    }

    @Override
    protected Element targetType(Element e) {
        return e;
    }

    @Override
    protected Element firstArgument(Element e) {
        AnnotationMirror annotationMirror = MoreElements.getAnnotationMirror(e, RequestSender.class).get();
        return getProviderInterface(annotationMirror);
    }

    private Element getProviderInterface(AnnotationMirror providerAnnotation) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> valueIndex =
                providerAnnotation.getElementValues();

        AnnotationValue value = valueIndex.values().iterator().next();
        return ((DeclaredType) value.getValue()).asElement();
    }

    @Override
    protected String annotation() {
        return RequestSender.class.getCanonicalName();
    }
}
