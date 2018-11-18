package org.dominokit.domino.apt.client.processors.handlers;


import com.squareup.javapoet.*;
import org.dominokit.domino.api.client.ServiceRootMatcher;
import org.dominokit.domino.api.client.annotations.Path;
import org.dominokit.domino.api.client.annotations.RequestSender;
import org.dominokit.domino.api.client.request.RequestRestSender;
import org.dominokit.domino.api.client.request.ServerRequestCallBack;
import org.dominokit.domino.api.shared.request.ArrayResponse;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;
import org.dominokit.domino.apt.commons.FullClassName;
import org.dominokit.domino.apt.commons.JavaSourceWriter;
import org.dominokit.domino.apt.commons.ProcessorElement;
import org.dominokit.rest.client.FailedResponse;
import org.dominokit.rest.shared.RestfulRequest;
//import org.fusesource.restygwt.client.*;

import javax.lang.model.element.Modifier;
//import javax.tools.Diagnostic;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.HttpMethod;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class RequestSenderSourceWriter extends JavaSourceWriter {

    private static final Map<String, AnnotationSpec> CONSUMES_ANNOTATIONS = new HashMap<>();

//    static {
//        AnnotationSpec consumesAnnotation = AnnotationSpec.builder(Consumes.class)
//                .addMember("value", "$T.APPLICATION_JSON", MediaType.class).build();
//        CONSUMES_ANNOTATIONS.put(HttpMethod.POST, consumesAnnotation);
//        CONSUMES_ANNOTATIONS.put(HttpMethod.PUT, consumesAnnotation);
//        CONSUMES_ANNOTATIONS.put("PATCH", consumesAnnotation);
//    }

    private final String serviceRoot;
    private final String method;
    private final List<String> pathParams = new ArrayList<>();
    private final String path;
    private final ClassName requestType;
    private final ClassName responseType;
    private final int[] successCodes;
    private final boolean arrayResponse;


    public RequestSenderSourceWriter(ProcessorElement processorElement, FullClassName request, FullClassName response, FullClassName fullSuperClassName) {
        super(processorElement);
        this.path = processorElement.getAnnotation(Path.class).value();
        this.serviceRoot = processorElement.getAnnotation(Path.class).serviceRoot();
        this.method = processorElement.getAnnotation(Path.class).method();
        this.successCodes = processorElement.getAnnotation(Path.class).successCodes();
        requestType = ClassName.get(request.asPackage(), request.asSimpleName());

        if(response.asImport().equals(new FullClassName(ArrayResponse.class.getCanonicalName()).asImport())) {
            FullClassName actualResponse=new FullClassName(fullSuperClassName.allImports().get(3));
            responseType = ClassName.get(actualResponse.asPackage(), actualResponse.asSimpleName());
            this.arrayResponse = true;
        }else{
            responseType = ClassName.get(response.asPackage(), response.asSimpleName());
            this.arrayResponse = false;
        }

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
        if (hasServiceRoot()) {
            senderBuilder.addField(addServiceRoot());
        }

        senderBuilder.addField(addPathField())
        .addField(addSuccessCodesField());

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

    private FieldSpec addSuccessCodesField() {
        return FieldSpec.builder(Integer[].class, "SUCCESS_CODES")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new Integer[]{"+getSuccessCodesString(successCodes)+"}").build();
    }

    private String getSuccessCodesString(int[] sucessCodes) {
        return IntStream.of(successCodes)
                .mapToObj(operand -> operand+"")
                .collect(Collectors.joining(","));
    }

//    private Iterable<ParameterSpec> pathParameters() {
//        return pathParams.stream().map(this::asParameterSpec).collect(toList());
//    }

//    private ParameterSpec asParameterSpec(String pathParam) {
//        AnnotationSpec pathParamAnnotation = AnnotationSpec.builder(PathParam.class)
//                .addMember("value", "\"" + pathParam + "\"").build();
//        AnnotationSpec attributeParamAnnotation = AnnotationSpec.builder(Attribute.class)
//                .addMember("value", "\"" + pathParam + "\"").build();
//        return ParameterSpec.builder(requestType, pathParam.replace(".", ""))
//                .addAnnotation(pathParamAnnotation)
//                .addAnnotation(attributeParamAnnotation)
//                .build();
//    }

    private boolean hasServiceRoot() {
        return !serviceRoot.trim().isEmpty();
    }




    private class SendMethodBuilder {
        private MethodSpec build() {
            MethodSpec.Builder sendMethodBuilder = MethodSpec.methodBuilder("send")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(requestType, "request")
                    .addParameter(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), ClassName.get(String.class)), "headers")
                    .addParameter(ServerRequestCallBack.class, "callBack");


            return sendMethodBuilder
                    .addCode(CodeBlock.builder()
                    .add("$T.post($T.matchedServiceRoot(PATH) + PATH)\n", RestfulRequest.class, ServiceRootMatcher.class)
                    .add(".putHeaders(headers)\n")
                    .add(".onSuccess(response ->")
                    .add(CodeBlock.builder().beginControlFlow("")
                            .beginControlFlow("if($T.stream(SUCCESS_CODES).anyMatch(code -> code.equals(response.getStatusCode())))", Arrays.class)
                            .add(getSuccessBlock())
                            .endControlFlow()
                            .beginControlFlow("else")
                            .addStatement("callBack.onFailure(new $T(response.getStatusCode(), response.getBodyAsString()))", FailedResponse.class)
                            .endControlFlow()
                            .endControlFlow()
                            .add(")")
                            .add(".onError(callBack::onFailure)\n")
                            .addStatement(".sendJson($T.INSTANCE.write(request))", ClassName.get(requestType.packageName(), requestType.simpleName()+"_MapperImpl"))
                            .build()).build())
                    .build();
        }


//
//        private void addServiceRootStatement(MethodSpec.Builder sendMethodBuilder) {
//            if (hasServiceRoot())
//                sendMethodBuilder.addStatement("(($T)service).setResource(new $T(SERVICE_ROOT, headers))", RestServiceProxy.class, Resource.class);
//            else
//                sendMethodBuilder.addStatement("(($T)service).setResource(new $T($T.matchedServiceRoot(PATH), headers))", RestServiceProxy.class, Resource.class, ServiceRootMatcher.class);
//        }

        private String makeParameters() {
            String params = "request";
            if (!pathParams.isEmpty())
                params = pathParams.stream().map(pathParam -> "request").collect(Collectors.joining(","));
            return params;
        }

//        private TypeSpec makeMethodCallback() {
//            return TypeSpec.anonymousClassBuilder("")
//                    .addSuperinterface(ParameterizedTypeName.get(ClassName.get(MethodCallback.class), responseType))
//                    .addMethod(makeOnFailureMethod())
//                    .addMethod(makeOnSuccessMethod())
//                    .build();
//        }

//        private MethodSpec makeOnFailureMethod() {
//            return MethodSpec.methodBuilder("onFailure")
//                    .addModifiers(Modifier.PUBLIC)
//                    .addAnnotation(Override.class)
//                    .addParameter(Method.class, "method")
//                    .addParameter(Throwable.class, "throwable")
//                    .addStatement("callBack.onFailure(throwable)")
//                    .build();
//        }
//
//        private MethodSpec makeOnSuccessMethod() {
//            return MethodSpec.methodBuilder("onSuccess")
//                    .addModifiers(Modifier.PUBLIC)
//                    .addAnnotation(Override.class)
//                    .addParameter(Method.class, "method")
//                    .addParameter(responseType, "response")
//                    .addStatement("callBack.onSuccess(response)")
//                    .build();
//        }

    }

    private CodeBlock getSuccessBlock() {


        CodeBlock.Builder builder = CodeBlock.builder();

        if(arrayResponse){
            builder
                    .addStatement("$T[] items = $T.INSTANCE.readArray(response.getBodyAsString(), length -> new $T[length])", responseType, ClassName.get(responseType.packageName(), responseType.simpleName()+"_MapperImpl"), responseType)
                    .addStatement("callBack.onSuccess(new $T<>(items))", ArrayResponse.class);
        }else {
            builder.addStatement("callBack.onSuccess($T.INSTANCE.read(response.getBodyAsString()))", ClassName.get(responseType.packageName(), responseType.simpleName()+"_MapperImpl"));
        }

        return builder.build();
    }
}
