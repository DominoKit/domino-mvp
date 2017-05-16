package com.progressoft.brix.domino.apt.client.initialtask;

import com.progressoft.brix.domino.apt.client.RegistrationImplementation;

import javax.lang.model.element.Element;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InitialTasksRegistrationImplementation implements RegistrationImplementation {

    public static final String REGISTRATION_LINE = "\tregistry.registerInitialTask";
    private List<? extends Element> items;

    public InitialTasksRegistrationImplementation(List<? extends Element> items) {
        this.items = items;
    }



    private String writeArguments(Element item) {
        return "new " + item.getSimpleName() + "()";
    }

    @Override
    public String methodBody() {
        return items.stream().map(item ->
                REGISTRATION_LINE + "(" + writeArguments(item) + ");\n\t")
                .collect(Collectors.joining());
    }

    @Override
    public String imports() {
        Set<String> imports = new HashSet<>();
        items.forEach(item ->
            imports.add("import " + item.asType().toString() + ";\n")
        );
        return imports.stream().map(String::toString).collect(Collectors.joining());
    }
}
