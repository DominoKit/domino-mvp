package com.progressoft.brix.domino.apt.client.request;

import com.progressoft.brix.domino.apt.client.RegistrationImplementation;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestRegistrationImplementation implements RegistrationImplementation {

    public static final String REGISTRATION_LINE = "\tregistry.registerRequest";
    private Map<Element, Element> requests;

    public RequestRegistrationImplementation(Map<Element, Element> requests) {
        this.requests = requests;
    }

    private String writeArguments(Map.Entry<Element, Element> entry) {
        return "" + getSimpleName(entry.getKey()) + ".class.getCanonicalName(), " +
                getSimpleName(entry.getValue()) + ".class.getCanonicalName()";
    }

    @Override
    public String methodBody() {
        return requests.entrySet().stream().map(entry ->
                REGISTRATION_LINE + "(" + writeArguments(entry) + ");\n\t")
                .collect(Collectors.joining());
    }

    @Override
    public String imports() {
        Set<String> imports = new HashSet<>();
        requests.entrySet().forEach(entry -> {
            imports.add("import " + ((TypeElement) entry.getKey()).getQualifiedName() + ";\n");
            imports.add("import " + ((TypeElement) entry.getValue()).getQualifiedName() + ";\n");
        });
        return imports.stream().map(String::toString).collect(Collectors.joining());
    }

    private String getSimpleName(Element e) {
        return e.getSimpleName().toString();
    }
}
