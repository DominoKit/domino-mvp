package com.progressoft.brix.domino.apt.client.path;

import com.progressoft.brix.domino.apt.client.RegistrationImplementation;

import javax.lang.model.element.Element;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PathRegistrationImplementation implements RegistrationImplementation {
    public static final String REGISTRATION_PAGE = "\tregistry.registerMapper";
    private Map<Element, String> items;

    public PathRegistrationImplementation(Map<Element, String> items) {
        this.items = items;
    }

    @Override
    public String methodBody() {
        return items.entrySet().stream().map(entry ->
                REGISTRATION_PAGE
                        + "(" + writeArguments(entry) + ");\n\t")
                .collect(Collectors.joining());
    }

    private String writeArguments(Map.Entry<Element, String> entry) {
        return "\"" + entry.getValue() + "\", " + new RequestFromPathResolver(entry).requestFromPathImplementation();
    }

    @Override
    public String imports() {
        Set<String> imports = new HashSet<>();
        items.entrySet().forEach(entry -> imports.addAll(new RequestFromPathResolver(entry).imports()));
        return imports.stream().collect(Collectors.joining());
    }
}
