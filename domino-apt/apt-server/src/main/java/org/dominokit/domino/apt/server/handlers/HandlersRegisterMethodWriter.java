package org.dominokit.domino.apt.server.handlers;

import org.dominokit.domino.api.server.handler.Handler;
import org.dominokit.domino.api.server.handler.HandlerRegistry;
import org.dominokit.domino.api.server.handler.RequestHandler;
import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;
import org.dominokit.domino.apt.commons.FullClassName;
import org.dominokit.domino.apt.commons.ProcessorElement;
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
        FullClassName request = new FullClassName(new FullClassName(entry.handler.getInterfaceFullQualifiedGenericName(RequestHandler.class)).allImports().get(1));
        methodBuilder.addStatement("registry.registerHandler(\"" + handlerAnnotation.value() + "\",new $T())", ClassName.get(entry.handler.elementPackage(), entry.handler.simpleName()));
    }

    @Override
    protected HandlersEntry parseEntry(ProcessorElement handler) {
        return new HandlersEntry(handler);
    }
}
