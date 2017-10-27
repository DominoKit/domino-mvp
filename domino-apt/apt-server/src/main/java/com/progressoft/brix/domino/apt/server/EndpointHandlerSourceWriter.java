package com.progressoft.brix.domino.apt.server;


import com.progressoft.brix.domino.api.server.endpoint.AbstractEndpointCallBackHandler;
import com.progressoft.brix.domino.api.server.endpoint.AbstractEndpointHandler;
import com.progressoft.brix.domino.api.server.handler.CallbackRequestHandler;
import com.progressoft.brix.domino.api.server.handler.RequestHandler;
import com.progressoft.brix.domino.apt.commons.DominoTypeBuilder;
import com.progressoft.brix.domino.apt.commons.JavaSourceWriter;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.List;

import static javax.tools.Diagnostic.Kind;

public class EndpointHandlerSourceWriter extends JavaSourceWriter {

    private TypeMirror requestType;
    private TypeMirror responseType;

    public EndpointHandlerSourceWriter(ProcessorElement element) {
        super(element);
        final List<? extends TypeMirror> typeArguments = getHandlerTypeArguments();
        requestType = typeArguments.get(0);
        responseType = typeArguments.get(1);
    }

    private List<? extends TypeMirror> getHandlerTypeArguments() {
        if (processorElement.isImplementsGenericInterface(RequestHandler.class))
            return ((DeclaredType) processorElement.getInterfaceType(RequestHandler.class)).getTypeArguments();
        return ((DeclaredType) processorElement.getInterfaceType(CallbackRequestHandler.class)).getTypeArguments();

    }

    @Override
    public String write() {
        TypeSpec endpoint = DominoTypeBuilder.build(processorElement.simpleName() +
                "EndpointHandler", EndpointsProcessor.class)
                .superclass(getSuperclass())
                .addMethod(makeRequestMethod())
                .addMethod(getRequestClassMethod())
                .build();

        StringBuilder builder = new StringBuilder();
        try {
            JavaFile.builder(processorElement.elementPackage(), endpoint).skipJavaLangImports(true).build().writeTo(builder);
        } catch (IOException e) {
            processorElement.getMessager().printMessage(Kind.ERROR, "Could not generate endpoint : " + e.getMessage(),
                    processorElement.getElement());
        }
        return builder.toString();
    }

    private ParameterizedTypeName getSuperclass() {
        if (processorElement.isImplementsGenericInterface(RequestHandler.class))
            return ParameterizedTypeName
                    .get(ClassName.get(AbstractEndpointHandler.class), TypeName.get(requestType),
                            TypeName.get(responseType));
        return ParameterizedTypeName
                .get(ClassName.get(AbstractEndpointCallBackHandler.class), TypeName.get(requestType),
                        TypeName.get(responseType));
    }


    private MethodSpec makeRequestMethod() {
        return MethodSpec.methodBuilder("makeNewRequest")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(TypeName.get(requestType))
                .addStatement("return new $T()", TypeName.get(requestType))
                .build();
    }

    private MethodSpec getRequestClassMethod() {
        return MethodSpec.methodBuilder("getRequestClass")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(ParameterizedTypeName.get(ClassName.get(Class.class), TypeName.get(requestType)))
                .addStatement("return $T.class", TypeName.get(requestType))
                .build();
    }
}
