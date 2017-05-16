package com.progressoft.brix.domino.apt.client.registration;

import com.google.auto.common.MoreElements;
import com.progressoft.brix.domino.api.client.annotations.UiView;
import com.progressoft.brix.domino.apt.client.ElementRegistration;
import com.progressoft.brix.domino.apt.client.RegistrationHelper;
import com.progressoft.brix.domino.apt.client.TwoArgumentsRegistrationFactory;
import com.progressoft.brix.domino.apt.client.uiview.ViewRegistration;
import com.progressoft.brix.domino.apt.client.uiview.ViewRegistrationImplementation;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import java.util.Map;

public class UiViewsRegistrationFactory extends TwoArgumentsRegistrationFactory {

    public UiViewsRegistrationFactory(RegistrationHelper helper) {
        super(helper);
    }

    @Override
    protected ElementRegistration typeRegistration() {
        return new ViewRegistration(new ViewRegistrationImplementation(items()));
    }

    @Override
    protected Element targetType(Element e) {
        AnnotationMirror annotationMirror = MoreElements.getAnnotationMirror(e, UiView.class).get();
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
        return UiView.class.getCanonicalName();
    }
}
