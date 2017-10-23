package com.progressoft.brix.domino.apt.client.processors.module.client;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.Set;

public abstract class AbstractRegisterMethodWriter<T extends AbstractRegisterMethodWriter.ItemEntry> {

    private final TypeSpec.Builder clientModuleTypeBuilder;

    public AbstractRegisterMethodWriter(TypeSpec.Builder clientModuleTypeBuilder) {
        this.clientModuleTypeBuilder = clientModuleTypeBuilder;
    }

    public void write(Set<String> items) {
        if (!items.isEmpty()) {
            MethodSpec.Builder registerViewsMethodBuilder = MethodSpec.methodBuilder(methodName())
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(registryClass(), "registry");
            items.stream().map(this::parseEntry)
                    .forEach(e -> registerItem(e, registerViewsMethodBuilder));
            clientModuleTypeBuilder.addMethod(registerViewsMethodBuilder.build());
        }
    }

    protected abstract String methodName();

    protected abstract Class<?> registryClass();

    protected abstract void registerItem(T entry, MethodSpec.Builder methodBuilder);

    protected abstract T parseEntry(String item);

    public interface ItemEntry {
    }
}
