package com.progressoft.brix.domino.apt.client;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class MethodWriter {

    private List<Modifier> modifiers;
    private String returnType;
    private String name;
    private Map<String, String> arguments;
    private RegistrationImplementation implementation;

    MethodWriter(List<Modifier> modifiers, String returnType, String name, Map<String, String> arguments, RegistrationImplementation implementation) {
        this.modifiers = modifiers;
        this.returnType = returnType;
        this.name = name;
        this.arguments = arguments;
        this.implementation = implementation;
    }

    String writeMethod() {
        return "\t" + appendModifiers()
                + appendReturnType()
                + appendName() + "(" + appendArgs() + ")" + "{\n"
                + "\t" + appendImplementation()
                + "" + "}\n\n";
    }

    private String appendModifiers() {
        return modifiers.stream().map(Modifier::toString).collect(Collectors.joining(" "));
    }

    private String appendReturnType() {
        return " " + returnType;
    }

    private String appendName() {
        return " " + name;
    }

    private String appendArgs() {
        return arguments.entrySet().stream().map(e -> e.getKey() + " " + e.getValue()).collect(Collectors.joining(","));
    }

    private String appendImplementation() {
        return implementation.methodBody();
    }

    public static class Builder {

        private List<Modifier> modifiers = new ArrayList<>();
        private String returnType;
        private String name;
        private Map<String, String> arguments = new HashMap<>();
        private RegistrationImplementation implementation;

        Builder modifier(Modifier modifier) {
            this.modifiers.add(modifier);
            return this;
        }

        Builder returnType(String returnType) {
            this.returnType = returnType;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        Builder argument(String type, String name) {
            this.arguments.put(type, name);
            return this;
        }

        Builder implementation(RegistrationImplementation implementation) {
            this.implementation = implementation;
            return this;
        }

        MethodWriter build() {
            return new MethodWriter(modifiers, returnType, name, arguments, implementation);
        }
    }
}
