package com.progressoft.brix.domino.apt.server.handlers;

import com.progressoft.brix.domino.api.server.handler.Handler;
import com.progressoft.brix.domino.api.server.handler.HandlerRegistry;
import com.progressoft.brix.domino.api.server.handler.RequestHandler;
import com.progressoft.brix.domino.apt.commons.AbstractRegisterMethodWriter;
import com.progressoft.brix.domino.apt.commons.FullClassName;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

public class HandlersRegisterMethodWriter extends AbstractRegisterMethodWriter<HandlersEntry, ProcessorElement> {

    public HandlersRegisterMethodWriter(TypeSpec.Builder serverModuleTypeBuilder) {
        super(serverModuleTypeBuilder);
    }

    @Override
    protected String methodName() {
        return "registerHandlers";
    }

    @Override
    protected Class<?> registryClass() {
        return HandlerRegistry.class;
    }

    @Override
    protected void registerItem(HandlersEntry entry, MethodSpec.Builder methodBuilder) {
        Handler handlerAnnotation = entry.handler.getAnnotation(Handler.class);
        String classifier = handlerAnnotation.classifier().isEmpty() ? "" : "+\"_" + handlerAnnotation.classifier() + "\"";
        FullClassName request = new FullClassName(new FullClassName(entry.handler.getInterfaceFullQualifiedGenericName(RequestHandler.class)).allImports().get(1));
        methodBuilder.addStatement("registry." + "registerHandler" + "($T.class.getCanonicalName()" + classifier + ",new $T())",
                ClassName.get(request.asPackage(), request.asSimpleName()), ClassName.get(entry.handler.elementPackage(), entry.handler.simpleName()));
    }

    @Override
    protected HandlersEntry parseEntry(ProcessorElement handler) {
        return new HandlersEntry(handler);
    }
}
