package com.progressoft.brix.domino.apt.client.request;

import com.progressoft.brix.domino.api.client.request.LazyRequestRestSenderLoader;
import com.progressoft.brix.domino.api.client.request.RequestRestSender;
import com.progressoft.brix.domino.apt.client.RegistrationImplementation;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestSenderRegistrationImplementation implements RegistrationImplementation {

    private static final String REGISTRATION_LINE = "\tregistry.registerRequestRestSender";
    private static final String IMPORT = "import ";
    private Map<Element, Element> requests;

    public RequestSenderRegistrationImplementation(Map<Element, Element> requests) {
        this.requests = requests;
    }

    private String writeArguments(Map.Entry<Element, Element> entry) {
        return "" + getSimpleName(entry.getKey()) + ".class.getCanonicalName(), " +
                " new LazyRequestRestSenderLoader() {\n" +
                "                            @Override\n" +
                "                            protected RequestRestSender make() {\n" +
                "                                return new " + getSimpleName(entry.getValue()) + "();\n" +
                "                            }\n" +
                "                        }";
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
        imports.add(IMPORT + LazyRequestRestSenderLoader.class.getCanonicalName() + ";\n");
        imports.add(IMPORT + RequestRestSender.class.getCanonicalName() + ";\n");
        requests.entrySet().forEach(entry -> {
            imports.add(IMPORT + ((TypeElement) entry.getKey()).getQualifiedName() + ";\n");
            imports.add(IMPORT + ((TypeElement) entry.getValue()).getQualifiedName() + ";\n");
        });
        return imports.stream().map(String::toString).collect(Collectors.joining());
    }

    private String getSimpleName(Element e) {
        return e.getSimpleName().toString();
    }
}
