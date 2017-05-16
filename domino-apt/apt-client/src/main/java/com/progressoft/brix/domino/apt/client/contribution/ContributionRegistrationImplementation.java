package com.progressoft.brix.domino.apt.client.contribution;

import com.progressoft.brix.domino.apt.client.RegistrationImplementation;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ContributionRegistrationImplementation implements RegistrationImplementation {

    public static final String REGISTRATION_LINE = "\tregistry.registerContribution";
    private Map<Element, Element> items;

    public ContributionRegistrationImplementation(Map<Element, Element> items) {
        this.items = items;
    }


    private String writeArguments(Map.Entry<Element, Element> entry) {
        return "" + getSimpleName(entry.getValue()) + ".class, " +
                "new " + getSimpleName(entry.getKey()) + "()";
    }

    @Override
    public String methodBody() {
        return items.entrySet().stream().map(entry ->
                REGISTRATION_LINE + "(" + writeArguments(entry) + ");\n\t")
                .collect(Collectors.joining());
    }

    @Override
    public String imports() {
        Set<String> imports = new HashSet<>();
        items.entrySet().forEach(entry -> {
            imports.add("import " + ((TypeElement) entry.getKey()).getQualifiedName() + ";\n");
            imports.add("import " + ((TypeElement) entry.getValue()).getQualifiedName() + ";\n");
        });
        return imports.stream().map(String::toString).collect(Collectors.joining());
    }

    private String getSimpleName(Element e) {
        return e.getSimpleName().toString();
    }
}
