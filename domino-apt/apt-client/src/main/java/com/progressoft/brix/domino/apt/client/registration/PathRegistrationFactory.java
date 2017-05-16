package com.progressoft.brix.domino.apt.client.registration;

import com.progressoft.brix.domino.api.client.annotations.Path;
import com.progressoft.brix.domino.api.client.request.BaseRequest;
import com.progressoft.brix.domino.apt.client.ElementRegistration;
import com.progressoft.brix.domino.apt.client.RegistrationHelper;
import com.progressoft.brix.domino.apt.client.SingleArgumentRegistrationFactory;
import com.progressoft.brix.domino.apt.client.path.PathRegistration;
import com.progressoft.brix.domino.apt.client.path.PathRegistrationImplementation;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import java.util.LinkedHashMap;
import java.util.Map;

public class PathRegistrationFactory extends SingleArgumentRegistrationFactory {
    public PathRegistrationFactory(RegistrationHelper helper) {
        super(helper);
    }

    @Override
    protected ElementRegistration typeRegistration() {
        return new PathRegistration(new PathRegistrationImplementation(paths()));
    }

    private Map<Element, String> paths() {
        Map<Element, String> paths = new LinkedHashMap<>();
        elements().forEach(e -> paths.put(e, pathValue(e)));
        return paths;
    }

    protected String pathValue(Element e) {
        AnnotationMirror annotationMirror = e.getAnnotationMirrors().stream()
                .filter(a -> a.getAnnotationType().toString().equals(annotation()))
                .findAny().orElseThrow(IllegalArgumentException::new);
        AnnotationValue path = annotationMirror.getElementValues().entrySet()
                .stream()
                .filter(entry -> !"path".equals(entry.getKey().getSimpleName())).findAny().orElseThrow(IllegalArgumentException::new).getValue();

        return (String) path.getValue();
    }

    @Override
    protected boolean isValid(Element e) {
        return helper.isSubtypeOfGenericClass(e, BaseRequest.class);
    }

    @Override
    protected String annotation() {
        return Path.class.getCanonicalName();
    }
}
