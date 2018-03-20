package org.dominokit.domino.apt.server;


import org.dominokit.domino.api.server.endpoint.AbstractEndpoint;
import org.dominokit.domino.api.server.handler.RequestHandler;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;
import org.dominokit.domino.apt.commons.JavaSourceWriter;
import org.dominokit.domino.apt.commons.ProcessorElement;
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
        return ((DeclaredType) processorElement.getInterfaceType(RequestHandler.class)).getTypeArguments();
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
        return ParameterizedTypeName
                .get(ClassName.get(AbstractEndpoint.class), TypeName.get(requestType),
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
