package org.dominokit.domino.apt.client.processors.handlers;

import com.squareup.javapoet.*;
import org.dominokit.domino.api.client.annotations.Path;
import org.dominokit.domino.api.client.annotations.RequestSender;
import org.dominokit.domino.api.client.request.AbstractArrayResponseRequestSender;
import org.dominokit.domino.api.client.request.AbstractSingleResponseRequestSender;
import org.dominokit.domino.api.shared.request.ArrayResponse;
import org.dominokit.domino.api.shared.request.VoidRequest;
import org.dominokit.domino.api.shared.request.VoidResponse;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;
import org.dominokit.domino.apt.commons.FullClassName;
import org.dominokit.domino.apt.commons.JavaSourceWriter;
import org.dominokit.domino.apt.commons.ProcessorElement;
import org.dominokit.jacksonapt.AbstractObjectMapper;

import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class RequestSenderSourceWriter extends JavaSourceWriter {

    private final String serviceRoot;
    private final String method;
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

        if (response.asImport().equals(new FullClassName(ArrayResponse.class.getCanonicalName()).asImport())) {
            FullClassName actualResponse = new FullClassName(fullSuperClassName.allImports().get(3));
            responseType = ClassName.get(actualResponse.asPackage(), actualResponse.asSimpleName());
            this.arrayResponse = true;
        } else {
            responseType = ClassName.get(response.asPackage(), response.asSimpleName());
            this.arrayResponse = false;
        }

    }

    @Override
    public String write() throws IOException {
        TypeSpec.Builder senderBuilder = makeSenderBuilder();
        if (hasServiceRoot()) {
            senderBuilder.addField(addServiceRoot());
        }

        senderBuilder
                .addField(addPathField())
                .addField(addSuccessCodesField());

        senderBuilder.addMethod(MethodSpec.methodBuilder("getPath")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(String.class)
                .addStatement("return PATH")
                .build());

        senderBuilder.addMethod(MethodSpec.methodBuilder("getSuccessCodes")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(Integer[].class)
                .addStatement("return SUCCESS_CODES")
                .build());

        senderBuilder.addMethod(MethodSpec.methodBuilder("getRequestMapper")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(ParameterizedTypeName.get(ClassName.get(AbstractObjectMapper.class), requestType))
                .addCode(getRequestMapper().build())
                .build());

        senderBuilder.addMethod(MethodSpec.methodBuilder("getResponseMapper")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(ParameterizedTypeName.get(ClassName.get(AbstractObjectMapper.class), responseType))
                .addCode(getResponseMapper().build())
                .build());

        senderBuilder.addMethod(MethodSpec.methodBuilder("getMethod")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(String.class)
                .addStatement("return \""+method+"\"")
                .build());

        senderBuilder.addMethod(MethodSpec.methodBuilder("getCustomRoot")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(String.class)
                .addStatement(hasServiceRoot()?"return SERVICE_ROOT":"return null")
                .build());

        senderBuilder.addMethod(new ReplaceParametersMethodBuilder().build());

        if(arrayResponse){
            senderBuilder.addMethod(MethodSpec.methodBuilder("initArray")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PROTECTED)
                    .returns(ArrayTypeName.of(responseType))
                    .addParameter(int.class, "length")
                    .addStatement("return new $T[length]", responseType)
                    .build());
        }

        StringBuilder asString = new StringBuilder();
        JavaFile.builder(processorElement.elementPackage(), senderBuilder.build()).skipJavaLangImports(true).build().writeTo(asString);
        return asString.toString();
    }

    private CodeBlock.Builder getRequestMapper() {
        CodeBlock.Builder builder = CodeBlock.builder();
        if (isVoidRequest()) {
            builder.addStatement("return null");
        }else{
            builder.addStatement("return $T.INSTANCE", ClassName.get(requestType.packageName(), requestType.simpleName() + "_MapperImpl"));
        }
        return builder;
    }

    private CodeBlock.Builder getResponseMapper() {
        CodeBlock.Builder builder = CodeBlock.builder();
        if (isVoidResponse()) {
            builder.addStatement("return null");
        }else{
            builder.addStatement("return $T.INSTANCE", ClassName.get(responseType.packageName(), responseType.simpleName() + "_MapperImpl"));
        }
        return builder;
    }

    private boolean isVoidRequest() {
        return requestType.compareTo(ClassName.get(VoidRequest.class)) == 0;
    }

    private boolean isVoidResponse() {
        return responseType.compareTo(ClassName.get(VoidResponse.class)) == 0;
    }

    private TypeSpec.Builder makeSenderBuilder() {
        AnnotationSpec.Builder requestSenderAnnotationBuilder = AnnotationSpec.builder(RequestSender.class)
                .addMember("value", CodeBlock.builder().add("$T.class", TypeName.get(processorElement.getElement().asType())).build());

        if (hasServiceRoot()) {
            requestSenderAnnotationBuilder.addMember("customServiceRoot", "true");
        }

        return DominoTypeBuilder.build(processorElement.simpleName() + "Sender", RequestPathProcessor.class)
                .addAnnotation(requestSenderAnnotationBuilder.build())
                .superclass(ParameterizedTypeName.get(ClassName.get(arrayResponse ? AbstractArrayResponseRequestSender.class : AbstractSingleResponseRequestSender.class), requestType, responseType));
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

    private class ReplaceParametersMethodBuilder {
        private MethodSpec build() {
            MethodSpec.Builder replaceParametersBuilder = MethodSpec.methodBuilder("replaceRequestParameters")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PROTECTED)
                    .returns(String.class)
                    .addParameter(String.class, "path")
                    .addParameter(requestType, "request");
            try {

                CodeBlock.Builder replaceBuilder = CodeBlock.builder()
                        .beginControlFlow("if (path.contains(\"{\"))")
                        .addStatement("$T processedPath = path", String.class);
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
                                .addStatement("return path")
                                .build());

                return replaceParametersBuilder.build();
            } catch (Exception ex) {
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
