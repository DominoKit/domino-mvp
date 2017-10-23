package com.progressoft.brix.domino.apt.client.processors.module.client.requests;

import com.progressoft.brix.domino.api.client.request.RequestRegistry;
import com.progressoft.brix.domino.apt.client.processors.module.client.AbstractRegisterMethodWriter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

public class RegisterRequestsMethodWriter extends AbstractRegisterMethodWriter<RequestEntry> {

    public RegisterRequestsMethodWriter(TypeSpec.Builder clientModuleTypeBuilder) {
        super(clientModuleTypeBuilder);
    }

    @Override
    protected String methodName() {
        return "registerRequests";
    }

    @Override
    protected Class<?> registryClass() {
        return RequestRegistry.class;
    }

    @Override
    protected void registerItem(RequestEntry entry, MethodSpec.Builder methodBuilder) {
        methodBuilder.addStatement("registry.registerRequest($T.class.getCanonicalName(), $T.class.getCanonicalName())"
                , ClassName.bestGuess(entry.request), ClassName.bestGuess(entry.presenter));
    }

    @Override
    protected RequestEntry parseEntry(String request) {
        String[] requestPair = request.split(":");
        return new RequestEntry(requestPair[0], requestPair[1]);
    }
}
