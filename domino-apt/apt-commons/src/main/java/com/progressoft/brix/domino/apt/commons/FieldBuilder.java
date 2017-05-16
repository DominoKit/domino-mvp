package com.progressoft.brix.domino.apt.commons;


public class FieldBuilder {
    private final String fieldName;
    private final JavaSourceBuilder javaSourceBuilder;

    private final StringBuilder modifiersWriter = new StringBuilder();
    private final StringBuilder typeWriter = new StringBuilder();
    private final StringBuilder fieldWriter=new StringBuilder();
    private final StringBuilder initialValueWriter=new StringBuilder();


    FieldBuilder(String name, JavaSourceBuilder javaSourceBuilder) {
        this.fieldName = name;
        this.javaSourceBuilder = javaSourceBuilder;
    }

    public FieldBuilder withModifier(ModifierBuilder.ModifierWriter modifierWriter) {
        modifiersWriter.append(modifierWriter.writeModifiers());
        return this;
    }

    public FieldBuilder ofType(String type) {
        return setType(new FullClassName(type));
    }

    private FieldBuilder setType(FullClassName type) {
        javaSourceBuilder.imports(type);
        typeWriter.append(type.asSimpleGenericName() + " ");
        return this;
    }

    public FieldBuilder initializedWith(String initializeString){
        initialValueWriter.append(" = ").append(initializeString);
        return this;
    }

    String write() {
        return fieldWriter
                .append("\n\t")
                .append(modifiersWriter.toString())
                .append(typeWriter.toString())
                .append(fieldName)
                .append(initialValueWriter.toString()).append(";\n")
                .toString();
    }
    public JavaSourceBuilder end() {
        return javaSourceBuilder.writeField(this);
    }

}
