package org.dominokit.domino.apt.client.processors.handlers;

import com.squareup.javapoet.*;
import com.sun.tools.internal.xjc.reader.TypeUtil;
import org.dominokit.domino.api.client.ServiceRootMatcher;
import org.dominokit.domino.api.client.annotations.Path;
import org.dominokit.domino.api.client.annotations.RequestSender;
import org.dominokit.domino.api.client.request.RequestRestSender;
import org.dominokit.domino.api.client.request.ServerRequestCallBack;
import org.dominokit.domino.api.shared.request.ArrayResponse;
import org.dominokit.domino.api.shared.request.VoidRequest;
import org.dominokit.domino.api.shared.request.VoidResponse;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;
import org.dominokit.domino.apt.commons.FullClassName;
import org.dominokit.domino.apt.commons.JavaSourceWriter;
import org.dominokit.domino.apt.commons.ProcessorElement;
import org.dominokit.rest.client.FailedResponse;
import org.dominokit.rest.shared.RestfulRequest;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class RequestSenderSourceWriter extends JavaSourceWriter {

    private final String serviceRoot;
    private final String method;
    private final List<String> pathParams = new ArrayList<>();
    private final String path;
    private final ClassName requestType;
    private final ClassName responseType;
    private final int[] successCodes;
    private final boolean arrayResponse;
    private final FullClassName requestFullName;


    public RequestSenderSourceWriter(ProcessorElement processorElement, FullClassName request, FullClassName response, FullClassName fullSuperClassName) {
        super(processorElement);
        this.path = processorElement.getAnnotation(Path.class).value();
        this.serviceRoot = processorElement.getAnnotation(Path.class).serviceRoot();
        this.method = processorElement.getAnnotation(Path.class).method();
        this.successCodes = processorElement.getAnnotation(Path.class).successCodes();
        requestType = ClassName.get(request.asPackage(), request.asSimpleName());
        this.requestFullName = request;

        if (response.asImport().equals(new FullClassName(ArrayResponse.class.getCanonicalName()).asImport())) {
            FullClassName actualResponse = new FullClassName(fullSuperClassName.allImports().get(3));
            responseType = ClassName.get(actualResponse.asPackage(), actualResponse.asSimpleName());
            this.arrayResponse = true;
        } else {
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
        senderBuilder.addMethod(new ReplaceParametersMethodBuilder().build());

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
                .initializer("new Integer[]{" + getSuccessCodesString() + "}").build();
    }

    private String getSuccessCodesString() {
        return IntStream.of(successCodes)
                .mapToObj(operand -> operand + "")
                .collect(Collectors.joining(","));
    }

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
                    .addParameter(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), ClassName.get(String.class)), "parameters")
                    .addParameter(ServerRequestCallBack.class, "callBack");

            CodeBlock.Builder sendRequestCodeBlock = CodeBlock.builder();
            if (requestType.compareTo(ClassName.get(VoidRequest.class)) == 0 || "get".equalsIgnoreCase(method)) {
                sendRequestCodeBlock.addStatement(".send()");
            } else {
                sendRequestCodeBlock.addStatement(".sendJson($T.INSTANCE.write(request))", ClassName.get(requestType.packageName(), requestType.simpleName() + "_MapperImpl"));
            }


            return sendMethodBuilder
                    .addCode(CodeBlock.builder()
                            .add("$T." + method.toLowerCase() + "($T.matchedServiceRoot(PATH) + replaceRequestParameters(request))\n", RestfulRequest.class, ServiceRootMatcher.class)
                            .add(".putHeaders(headers)\n")
                            .add(".putParameters(parameters)\n")
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
                                    .add(sendRequestCodeBlock
                                            .build())
                                    .build()).build())
                    .build();
        }


        private CodeBlock getSuccessBlock() {

            CodeBlock.Builder builder = CodeBlock.builder();

            if (responseType.compareTo(ClassName.get(VoidResponse.class)) == 0) {
                builder.addStatement("callBack.onSuccess(new $T())", ClassName.get(VoidResponse.class));
            } else {
                if (arrayResponse) {
                    builder
                            .addStatement("$T[] items = $T.INSTANCE.readArray(response.getBodyAsString(), length -> new $T[length])", responseType, ClassName.get(responseType.packageName(), responseType.simpleName() + "_MapperImpl"), responseType)
                            .addStatement("callBack.onSuccess(new $T<>(items))", ArrayResponse.class);
                } else {
                    builder.addStatement("callBack.onSuccess($T.INSTANCE.read(response.getBodyAsString()))", ClassName.get(responseType.packageName(), responseType.simpleName() + "_MapperImpl"));
                }
            }

            return builder.build();
        }

    }

    private class ReplaceParametersMethodBuilder {
        private MethodSpec build() {
            MethodSpec.Builder replaceParametersBuilder = MethodSpec.methodBuilder("replaceRequestParameters")
                    .addModifiers(Modifier.PRIVATE)
                    .returns(String.class)
                    .addParameter(requestType, "request");
            try {

                CodeBlock.Builder replaceBuilder = CodeBlock.builder()
                        .beginControlFlow("if (PATH.contains(\"{\"))")
                        .addStatement("$T processedPath = PATH", String.class);
                String tempPath = path;
                while (tempPath.contains("{")) {
                    String nextParameter = tempPath.substring(tempPath.indexOf("{"), tempPath.indexOf("}") + 1);
                    tempPath = tempPath.replace(nextParameter, convertParameterToGetter(nextParameter));
                    replaceBuilder.addStatement("processedPath = processedPath.replace(\"" + nextParameter + "\", " + convertParameterToGetter(nextParameter) + "+\"\")", Objects.class);
                }
                replaceBuilder.addStatement("return processedPath");

                replaceBuilder.endControlFlow();
                replaceParametersBuilder
                        .addCode(CodeBlock.builder()
                                .add(replaceBuilder.build())
                                .addStatement("return PATH")
                                .build());

                return replaceParametersBuilder.build();
            }catch (Exception ex){
                handleError(ex);
            }

            return replaceParametersBuilder.build();
        }


        private String convertParameterToGetter(String parameter) {
            String names = parameter
                    .replace("{", "")
                    .replace("}", "");

            String[] fieldsNames = names.contains(".") ? names.split("\\.") : new String[]{names};

            List<String> gettersString = Arrays.asList(fieldsNames)
                    .stream()
                    .map(s -> "get" + s.replace(s.charAt(0) + "", (s.charAt(0) + "").toUpperCase()) + "()")
                    .collect(Collectors.toList());

            String result = getterWithNullCheck(gettersString, 1);

            return result;
        }

        private String getterWithNullCheck(List<String> getters, int upTo) {

            if (upTo <= getters.size()) {
                return "$1T.isNull(request." + getters.subList(0, upTo).stream().collect(Collectors.joining(".")) + ")?null:(" + getterWithNullCheck(getters, ++upTo) + ")";
            } else {
                return "request." + getters.subList(0, upTo - 1).stream().collect(Collectors.joining("."));
            }
        }
    }


    protected void handleError(Exception e) {
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        processorElement.getMessager().printMessage(Diagnostic.Kind.ERROR, "error while creating source file " + out.getBuffer().toString());
    }

}
