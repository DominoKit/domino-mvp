package org.dominokit.domino.apt.client.processors.handlers;


import com.google.gwt.core.client.GWT;
import org.dominokit.domino.api.client.ServiceRootMatcher;
import org.dominokit.domino.api.client.annotations.Path;
import org.dominokit.domino.api.client.annotations.RequestSender;
import org.dominokit.domino.api.client.request.RequestRestSender;
import org.dominokit.domino.api.client.request.ServerRequestCallBack;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;
import org.dominokit.domino.apt.commons.FullClassName;
import org.dominokit.domino.apt.commons.JavaSourceWriter;
import org.dominokit.domino.apt.commons.ProcessorElement;
import com.squareup.javapoet.*;
import org.fusesource.restygwt.client.*;

import javax.lang.model.element.Modifier;
import javax.ws.rs.Consumes;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class RequestSenderSourceWriter extends JavaSourceWriter {

    private static final Map<String, AnnotationSpec> CONSUMES_ANNOTATIONS = new HashMap<>();

    static {
        AnnotationSpec consumesAnnotation = AnnotationSpec.builder(Consumes.class)
                .addMember("value", "$T.APPLICATION_JSON", MediaType.class).build();
        CONSUMES_ANNOTATIONS.put(HttpMethod.POST, consumesAnnotation);
        CONSUMES_ANNOTATIONS.put(HttpMethod.PUT, consumesAnnotation);
        CONSUMES_ANNOTATIONS.put("PATCH", consumesAnnotation);
    }

    private final String serviceRoot;
    private final String method;
    private final List<String> pathParams = new ArrayList<>();
    private final String path;
    private final ClassName requestType;
    private final ClassName responseType;


    public RequestSenderSourceWriter(ProcessorElement processorElement, FullClassName request, FullClassName response) {
        super(processorElement);
        this.path = processorElement.getAnnotation(Path.class).value();
        this.serviceRoot = processorElement.getAnnotation(Path.class).serviceRoot();
        this.method = processorElement.getAnnotation(Path.class).method();
        requestType = ClassName.get(request.asPackage(), request.asSimpleName());
        responseType = ClassName.get(response.asPackage(), response.asSimpleName());
        fillPathParameters(path);
    }

    private void fillPathParameters(String path) {
        Matcher pathParamMatcher = Pattern.compile("\\{(.*?)\\}").matcher(path);
        while (pathParamMatcher.find()) {
            pathParams.add(pathParamMatcher.group().replace("{", "").replace("}", ""));
        }
    }

    @Override
    public String write() throws IOException {
        TypeSpec.Builder senderBuilder = makeSenderBuilder();
        if (hasServiceRoot())
            senderBuilder.addField(addServiceRoot());
        senderBuilder.addField(addPathField());
        TypeSpec serviceInterface = new ServiceInterfaceBuilder().build();
        senderBuilder.addType(serviceInterface);


        ClassName serviceInterfaceType = ClassName.bestGuess(serviceInterface.name);
        FieldSpec serviceInterfaceField = FieldSpec.builder(serviceInterfaceType, "service")
                .addModifiers(Modifier.PRIVATE)
                .initializer("$T.create($T.class)", GWT.class, serviceInterfaceType)
                .build();

        senderBuilder.addField(serviceInterfaceField);

        senderBuilder.addMethod(new SendMethodBuilder().build());

        StringBuilder asString = new StringBuilder();
        JavaFile.builder(processorElement.elementPackage(), senderBuilder.build()).skipJavaLangImports(true).build().writeTo(asString);
        return asString.toString();
    }

    private TypeSpec.Builder makeSenderBuilder() {
        AnnotationSpec.Builder requestSenderAnnotationBuilder = AnnotationSpec.builder(RequestSender.class)
                .addMember("value", CodeBlock.builder().add("$T.class", TypeName.get(processorElement.getElement().asType())).build());

        if (hasServiceRoot())
            requestSenderAnnotationBuilder.addMember("customServiceRoot", "true");

        return DominoTypeBuilder.build(processorElement.simpleName() + "Sender", RequestPathProcessor.class)
                .addAnnotation(requestSenderAnnotationBuilder.build())
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(RequestRestSender.class), requestType));
    }

    private FieldSpec addServiceRoot() {
        return FieldSpec.builder(String.class, "SERVICE_ROOT")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("\"" + serviceRoot + "\"").build();
    }

    private FieldSpec addPathField() {
        return FieldSpec.builder(String.class, "PATH")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("\"" + path + "\"").build();
    }

    private Iterable<ParameterSpec> pathParameters() {
        return pathParams.stream().map(this::asParameterSpec).collect(toList());
    }

    private ParameterSpec asParameterSpec(String pathParam) {
        AnnotationSpec pathParamAnnotation = AnnotationSpec.builder(PathParam.class)
                .addMember("value", "\"" + pathParam + "\"").build();
        AnnotationSpec attributeParamAnnotation = AnnotationSpec.builder(Attribute.class)
                .addMember("value", "\"" + pathParam + "\"").build();
        return ParameterSpec.builder(requestType, pathParam.replace(".", ""))
                .addAnnotation(pathParamAnnotation)
                .addAnnotation(attributeParamAnnotation)
                .build();
    }

    private boolean hasServiceRoot() {
        return !serviceRoot.trim().isEmpty();
    }


    private class ServiceInterfaceBuilder {
        private TypeSpec build() {
            MethodSpec.Builder sendMethodBuilder = MethodSpec.methodBuilder("send").addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC);

            if (pathParams.isEmpty()) {
                sendMethodBuilder.addParameter(requestType, "request");
            } else {
                sendMethodBuilder.addParameters(pathParameters());
            }

            sendMethodBuilder.addParameter(ParameterizedTypeName.get(ClassName.get(MethodCallback.class), responseType), "callback");

            sendMethodBuilder.addAnnotation(AnnotationSpec.builder(ClassName.get("javax.ws.rs", method)).build())
                    .addAnnotation(AnnotationSpec.builder(javax.ws.rs.Path.class).addMember("value", "PATH").build())
                    .addAnnotation(AnnotationSpec.builder(Produces.class).addMember("value", "$T.APPLICATION_JSON", MediaType.class).build());

            if (CONSUMES_ANNOTATIONS.containsKey(method))
                sendMethodBuilder.addAnnotation(CONSUMES_ANNOTATIONS.get(method));

            return TypeSpec.interfaceBuilder(processorElement.simpleName() + "Service")
                    .addSuperinterface(RestService.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(sendMethodBuilder.build())
                    .build();
        }
    }

    private class SendMethodBuilder {
        private MethodSpec build() {
            MethodSpec.Builder sendMethodBuilder = MethodSpec.methodBuilder("send")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(requestType, "request")
                    .addParameter(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), ClassName.get(String.class)), "headers")
                    .addParameter(ServerRequestCallBack.class, "callBack");

            addServiceRootStatement(sendMethodBuilder);

            return sendMethodBuilder.addStatement("service.send(" + makeParameters() + ", $L)", makeMethodCallback()).build();
        }

        private void addServiceRootStatement(MethodSpec.Builder sendMethodBuilder) {
            if (hasServiceRoot())
                sendMethodBuilder.addStatement("(($T)service).setResource(new $T(SERVICE_ROOT, headers))", RestServiceProxy.class, Resource.class);
            else
                sendMethodBuilder.addStatement("(($T)service).setResource(new $T($T.matchedServiceRoot(PATH), headers))", RestServiceProxy.class, Resource.class, ServiceRootMatcher.class);
        }

        private String makeParameters() {
            String params = "request";
            if (!pathParams.isEmpty())
                params = pathParams.stream().map(pathParam -> "request").collect(Collectors.joining(","));
            return params;
        }

        private TypeSpec makeMethodCallback() {
            return TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(ParameterizedTypeName.get(ClassName.get(MethodCallback.class), responseType))
                    .addMethod(makeOnFailureMethod())
                    .addMethod(makeOnSuccessMethod())
                    .build();
        }

        private MethodSpec makeOnFailureMethod() {
            return MethodSpec.methodBuilder("onFailure")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(Method.class, "method")
                    .addParameter(Throwable.class, "throwable")
                    .addStatement("callBack.onFailure(throwable)")
                    .build();
        }

        private MethodSpec makeOnSuccessMethod() {
            return MethodSpec.methodBuilder("onSuccess")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(Method.class, "method")
                    .addParameter(responseType, "response")
                    .addStatement("callBack.onSuccess(response)")
                    .build();
        }

    }
}
