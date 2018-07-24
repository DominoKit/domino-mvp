package org.dominokit.domino.apt.client.processors.module.client.listeners;

import org.dominokit.domino.api.client.extension.DominoEventsRegistry;
import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

public class RegisterListenersMethodWriter extends AbstractRegisterMethodWriter<ListenerEntry, String> {

    public RegisterListenersMethodWriter(TypeSpec.Builder clientModuleTypeBuilder) {
        super(clientModuleTypeBuilder);
    }

    @Override
    protected String methodName() {
        return "registerListeners";
    }

    @Override
    protected Class<?> registryClass() {
        return DominoEventsRegistry.class;
    }

    @Override
    protected void registerItem(ListenerEntry entry, MethodSpec.Builder methodBuilder) {
        methodBuilder.addStatement("registry.addListener($T.class, new $T())", ClassName.bestGuess(entry.dominoEvent), ClassName.bestGuess(entry.listener));
    }

    @Override
    protected ListenerEntry parseEntry(String request) {
        String[] requestPair = request.split(":");
        return new ListenerEntry(requestPair[0], requestPair[1]);
    }
}
