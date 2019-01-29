package org.dominokit.domino.apt.commons;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.Collection;

import static java.util.Objects.nonNull;

public abstract class AbstractRegisterMethodWriter<E extends AbstractRegisterMethodWriter.ItemEntry, I> {

    private final TypeSpec.Builder clientModuleTypeBuilder;

    public AbstractRegisterMethodWriter(TypeSpec.Builder clientModuleTypeBuilder) {
        this.clientModuleTypeBuilder = clientModuleTypeBuilder;
    }

    public void write(Collection<I> items) {
        if (!items.isEmpty()) {
            MethodSpec.Builder registerViewsMethodBuilder = MethodSpec.methodBuilder(methodName())
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC);

            if(nonNull(registryClass())) {
                    registerViewsMethodBuilder.addParameter(registryClass(), "registry");
            }
            items.stream().map(this::parseEntry)
                    .forEach(e -> registerItem(e, registerViewsMethodBuilder));
            clientModuleTypeBuilder.addMethod(registerViewsMethodBuilder.build());
        }
    }

    protected abstract String methodName();

    protected abstract Class<?> registryClass();

    protected abstract void registerItem(E entry, MethodSpec.Builder methodBuilder);

    protected abstract E parseEntry(I item);

    public interface ItemEntry {
    }
}
