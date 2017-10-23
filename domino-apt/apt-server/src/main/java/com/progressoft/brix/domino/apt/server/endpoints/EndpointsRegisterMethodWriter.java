package com.progressoft.brix.domino.apt.server.endpoints;

import com.progressoft.brix.domino.api.server.endpoint.EndpointsRegistry;
import com.progressoft.brix.domino.api.server.handler.Handler;
import com.progressoft.brix.domino.apt.commons.AbstractRegisterMethodWriter;
import com.progressoft.brix.domino.apt.commons.FullClassName;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

public class EndpointsRegisterMethodWriter extends AbstractRegisterMethodWriter<EndpointsEntry, ProcessorElement> {
    public EndpointsRegisterMethodWriter(TypeSpec.Builder clientModuleTypeBuilder) {
        super(clientModuleTypeBuilder);
    }

    @Override
    protected String methodName() {
        return "registerEndpoints";
    }

    @Override
    protected Class<?> registryClass() {
        return EndpointsRegistry.class;
    }

    @Override
    protected void registerItem(EndpointsEntry entry, MethodSpec.Builder methodBuilder) {
        final FullClassName handlerEndpoint = new FullClassName(entry.element.fullQualifiedNoneGenericName() + "EndpointHandler");
        String path = entry.element.getAnnotation(Handler.class).value();

        ClassName handlerEndpointType = ClassName.get(handlerEndpoint.asPackage(), handlerEndpoint.asSimpleName());
        MethodSpec getMethod = MethodSpec.methodBuilder("get")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(handlerEndpointType)
                .addStatement("return new $T()", handlerEndpointType)
                .build();
        TypeSpec factoryType = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(EndpointsRegistry.EndpointHandlerFactory.class)
                .addMethod(getMethod)
                .build();
        methodBuilder.addStatement("registry.registerEndpoint(\"" + path + "\", $L)", factoryType);
    }

    @Override
    protected EndpointsEntry parseEntry(ProcessorElement item) {
        return new EndpointsEntry(item);
    }
}
