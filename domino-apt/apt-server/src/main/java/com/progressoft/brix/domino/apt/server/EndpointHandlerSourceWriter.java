package com.progressoft.brix.domino.apt.server;


import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.server.entrypoint.VertxEntryPointContext;
import com.progressoft.brix.domino.api.server.handler.CallbackRequestHandler;
import com.progressoft.brix.domino.api.server.handler.RequestHandler;
import com.progressoft.brix.domino.apt.commons.FullClassName;
import com.progressoft.brix.domino.apt.commons.JavaSourceBuilder;
import com.progressoft.brix.domino.apt.commons.JavaSourceWriter;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;
import com.squareup.javapoet.*;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import javax.annotation.Generated;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.List;

public class EndpointHandlerSourceWriter extends JavaSourceWriter {

    private static final int REQUEST_IMPORT_INDEX = 1;
    private static final int RESPONSE_IMPORT_INDEX = 2;
    private final JavaSourceBuilder sourceBuilder;
    private TypeMirror requestType;
    private TypeMirror responseType;

    public EndpointHandlerSourceWriter(ProcessorElement element) {
        super(element);
        this.sourceBuilder = new JavaSourceBuilder(element.simpleName() +
                "EndpointHandler");
        List<String> handlerImports;
        if (element.isImplementsGenericInterface(RequestHandler.class)) {
            handlerImports = new FullClassName(processorElement.getInterfaceFullQualifiedGenericName(RequestHandler.class)).allImports();
        } else {
            handlerImports = new FullClassName(processorElement.getInterfaceFullQualifiedGenericName(CallbackRequestHandler.class)).allImports();
        }


        responseType = processorElement.getElementUtils().getTypeElement(new FullClassName(handlerImports.get(RESPONSE_IMPORT_INDEX)).asImport()).asType();
        requestType = processorElement.getElementUtils().getTypeElement(new FullClassName(handlerImports.get(REQUEST_IMPORT_INDEX)).asImport()).asType();
    }

    @Override
    public String write() {

        AnnotationSpec generatedAnnotation = AnnotationSpec.builder(Generated.class)
                .addMember("value", "\""+EndpointsProcessor.class.getCanonicalName()+"\"")
                .build();
        TypeSpec endpoint = TypeSpec.classBuilder(processorElement.simpleName() +
                "EndpointHandler")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(Handler.class, RoutingContext.class))
                .addMethod(writeBody())
                .addAnnotation(generatedAnnotation)
                .build();

        StringBuilder builder = new StringBuilder();
        try {
            JavaFile.builder(processorElement.elementPackage(), endpoint).build().writeTo(builder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }


    private MethodSpec writeBody() {
        return MethodSpec.methodBuilder("handle")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(RoutingContext.class, "routingContext")
                .beginControlFlow("try")
                .addCode(buildBody())
                .nextControlFlow("catch($T e)", Exception.class)
                .addStatement("routingContext.fail(e)")
                .endControlFlow()
                .build();
    }


    private CodeBlock buildBody() {
        return CodeBlock.builder()
                .addStatement("$1T serverApp = $1T.make()", ServerApp.class)
                .addStatement("$T requestBody", requestType)
                .addStatement("$T method = routingContext.request().method()", HttpMethod.class)
                .beginControlFlow("if($1T.POST.equals(method) || $1T.PUT.equals(method) || $1T.PATCH.equals(method))", HttpMethod.class)
                .addStatement("requestBody=$T.decodeValue(routingContext.getBodyAsString(), $T.class)", Json.class, requestType)
                .nextControlFlow("else")
                .addStatement("requestBody = new $T()", requestType)
                .addStatement("requestBody.setRequestKey($T.class.getCanonicalName())", requestType)
                .endControlFlow()
                .add(evaluateHandler())
                .build();
    }

    private CodeBlock evaluateHandler() {
        if (processorElement.isImplementsGenericInterface(RequestHandler.class))
            return completeHandler();
        else
            return completeHandlerCallback();
    }

    private CodeBlock completeHandler() {

        return CodeBlock.builder()
                .addStatement("$1T response=($1T)serverApp.executeRequest(requestBody, new $2T(routingContext, serverApp.serverContext().config(), routingContext.vertx()))", responseType, VertxEntryPointContext.class)
                .addStatement("routingContext.response()\n" +
                        "                .putHeader(\"content-type\", \"application/json\")\n" +
                        "                .end($T.encode(response))", Json.class)
                .build();
    }

    private CodeBlock completeHandlerCallback() {
        sourceBuilder.imports(CallbackRequestHandler.class.getCanonicalName());
        MethodSpec completeMethod = MethodSpec.methodBuilder("complete")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(Object.class, "response")
                .addStatement("routingContext.response()" +
                        ".putHeader(\"content-type\", \"application/json\")" +
                        ".end($T.encode(($T)response))", Json.class, responseType).build();

        MethodSpec redirectMethod = MethodSpec.methodBuilder("redirect")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(String.class, "location")
                .addStatement("routingContext.response().setStatusCode(302).putHeader(\"Location\",location).end()").build();


        TypeSpec responseCallback = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(CallbackRequestHandler.ResponseCallback.class)
                .addMethod(completeMethod)
                .addMethod(redirectMethod)
                .build();

        return CodeBlock.builder()
                .addStatement("serverApp.executeCallbackRequest(requestBody, new $T(routingContext, serverApp.serverContext().config(), routingContext.vertx()), $L)", VertxEntryPointContext.class, responseCallback).build();

    }
}
