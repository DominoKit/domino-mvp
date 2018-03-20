package org.dominokit.domino.apt.server.endpoints;

import org.dominokit.domino.api.server.endpoint.EndpointsRegistry;
import org.dominokit.domino.api.server.handler.Handler;
import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;
import org.dominokit.domino.apt.commons.FullClassName;
import org.dominokit.domino.apt.commons.ProcessorElement;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.function.Supplier;

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
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(Supplier.class), handlerEndpointType.box()))
                .addMethod(getMethod)
                .build();
        methodBuilder.addStatement("registry.registerEndpoint(\"" + path + "\", $L)", factoryType);
    }

    @Override
    protected EndpointsEntry parseEntry(ProcessorElement item) {
        return new EndpointsEntry(item);
    }
}
