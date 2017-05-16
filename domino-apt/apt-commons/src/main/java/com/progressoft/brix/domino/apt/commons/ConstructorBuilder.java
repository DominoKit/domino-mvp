package com.progressoft.brix.domino.apt.commons;

public class ConstructorBuilder extends MethodBuilder{

    ConstructorBuilder(String name, JavaSourceBuilder javaSourceBuilder) {
        super(name, javaSourceBuilder);
    }

    @Override
    public MethodBuilder returns(String type) {
        return this;
    }
}
