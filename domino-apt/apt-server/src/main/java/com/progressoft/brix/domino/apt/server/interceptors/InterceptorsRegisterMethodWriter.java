package com.progressoft.brix.domino.apt.server.interceptors;

import com.progressoft.brix.domino.api.server.interceptor.InterceptorsRegistry;
import com.progressoft.brix.domino.api.server.interceptor.RequestInterceptor;
import com.progressoft.brix.domino.apt.commons.AbstractRegisterMethodWriter;
import com.progressoft.brix.domino.apt.commons.FullClassName;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

public class InterceptorsRegisterMethodWriter extends AbstractRegisterMethodWriter<InterceptorsEntry, ProcessorElement> {
    public InterceptorsRegisterMethodWriter(TypeSpec.Builder clientModuleTypeBuilder) {
        super(clientModuleTypeBuilder);
    }

    @Override
    protected String methodName() {
        return "registerInterceptors";
    }

    @Override
    protected Class<?> registryClass() {
        return InterceptorsRegistry.class;
    }

    @Override
    protected void registerItem(InterceptorsEntry entry, MethodSpec.Builder methodBuilder) {
        FullClassName request = new FullClassName(new FullClassName(entry.element.getInterfaceFullQualifiedGenericName(RequestInterceptor.class)).allImports().get(1));
        FullClassName entryPoint = new FullClassName(new FullClassName(entry.element.getInterfaceFullQualifiedGenericName(RequestInterceptor.class)).allImports().get(2));
        methodBuilder.addStatement("registry.registerInterceptor($T.class.getCanonicalName(), $T.class.getCanonicalName(), new $T())",
                ClassName.bestGuess(request.asSimpleGenericName()), ClassName.bestGuess(entryPoint.asSimpleGenericName()), ClassName.bestGuess(entry.element.simpleName()));
    }

    @Override
    protected InterceptorsEntry parseEntry(ProcessorElement element) {
        return new InterceptorsEntry(element);
    }
}
