package com.progressoft.brix.domino.apt.server.interceptors.global;

import com.progressoft.brix.domino.api.server.interceptor.GlobalRequestInterceptor;
import com.progressoft.brix.domino.api.server.interceptor.InterceptorsRegistry;
import com.progressoft.brix.domino.apt.commons.AbstractRegisterMethodWriter;
import com.progressoft.brix.domino.apt.commons.FullClassName;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

public class GlobalInterceptorsRegisterMethodWriter extends AbstractRegisterMethodWriter<GlobalInterceptorsEntry, ProcessorElement> {
    public GlobalInterceptorsRegisterMethodWriter(TypeSpec.Builder clientModuleTypeBuilder) {
        super(clientModuleTypeBuilder);
    }

    @Override
    protected String methodName() {
        return "registerGlobalInterceptors";
    }

    @Override
    protected Class<?> registryClass() {
        return InterceptorsRegistry.class;
    }

    @Override
    protected void registerItem(GlobalInterceptorsEntry entry, MethodSpec.Builder methodBuilder) {
        FullClassName entryPoint = new FullClassName(new FullClassName(entry.element.getInterfaceFullQualifiedGenericName(GlobalRequestInterceptor.class)).allImports().get(1));
        methodBuilder.addStatement("registry.registerGlobalInterceptor($T.class.getCanonicalName(), new $T())",
                ClassName.get(entryPoint.asPackage(), entryPoint.asSimpleGenericName()), ClassName.get(entry.element.elementPackage(), entry.element.simpleName()));
    }

    @Override
    protected GlobalInterceptorsEntry parseEntry(ProcessorElement item) {
        return new GlobalInterceptorsEntry(item);
    }
}
