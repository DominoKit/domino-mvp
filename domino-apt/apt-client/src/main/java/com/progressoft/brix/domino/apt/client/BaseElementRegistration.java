package com.progressoft.brix.domino.apt.client;

import javax.lang.model.element.Modifier;

public abstract class BaseElementRegistration implements ElementRegistration {

    private RegistrationImplementation implementation;

    public BaseElementRegistration(RegistrationImplementation implementation) {
        this.implementation = implementation;
    }

    @Override
    public String registrationMethod() {
        return "\t@Override\n" + new MethodWriter.Builder().modifier(Modifier.PUBLIC)
                .returnType("void")
                .name(methodName())
                .argument(argumentType().getSimpleName(), "registry")
                .implementation(implementation)
                .build().writeMethod();
    }

    @Override
    public String imports() {
        StringBuilder builder = new StringBuilder();
        builder.append("import " + argumentType().getCanonicalName() + ";\n");
        builder.append(implementation.imports());
        return builder.toString();
    }

    protected abstract Class<?> argumentType();

    protected abstract String methodName();
}
