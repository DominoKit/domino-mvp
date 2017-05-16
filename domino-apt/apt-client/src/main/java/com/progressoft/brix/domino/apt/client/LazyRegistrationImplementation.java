package com.progressoft.brix.domino.apt.client;

import javax.lang.model.element.Element;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class LazyRegistrationImplementation implements RegistrationImplementation {

    public static final String METHOD_CLOSING = "\t\t});\n";
    private Map<Element, Element> items;

    public LazyRegistrationImplementation(Map<Element, Element> items) {
        this.items = items;
    }

    @Override
    public String methodBody() {
        return items.entrySet().stream().map(entry ->
                writeRegistrationLine() + "(" + writeArguments(entry) + ") {\n" +
                        writeLazyImplementation(entry) +
                        METHOD_CLOSING).collect(Collectors.joining());
    }

    protected abstract String writeRegistrationLine();

    protected abstract String writeArguments(Map.Entry<Element, Element> entry);

    protected abstract String writeLazyImplementation(Map.Entry<Element, Element> entry);

    @Override
    public String imports() {
        Set<String> imports = new HashSet<>();
        imports.addAll(implementationImports());
        items.entrySet().forEach(entry -> {
            imports.add("import " + entry.getKey() + ";\n");
            imports.add("import " + entry.getValue() + ";\n");
        });
        return imports.stream().map(String::toString).collect(Collectors.joining());
    }

    protected abstract Collection<String> implementationImports();

    protected String getSimpleName(Element e) {
        return e.getSimpleName().toString();
    }
}
