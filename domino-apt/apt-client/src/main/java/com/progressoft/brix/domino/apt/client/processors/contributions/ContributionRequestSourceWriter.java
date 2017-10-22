package com.progressoft.brix.domino.apt.client.processors.contributions;

import com.progressoft.brix.domino.api.client.annotations.Request;
import com.progressoft.brix.domino.api.client.request.ClientRequest;
import com.progressoft.brix.domino.api.shared.extension.Contribution;
import com.progressoft.brix.domino.apt.commons.FullClassName;
import com.progressoft.brix.domino.apt.commons.JavaSourceWriter;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;
import com.squareup.javapoet.*;

import javax.annotation.Generated;
import javax.lang.model.element.Modifier;
import java.io.IOException;

public class ContributionRequestSourceWriter extends JavaSourceWriter {

    private final String targetPackage;
    private final String generatedClassName;
    private final String presenterMethod;
    private final ClassName presenterType;
    private final ClassName contextType;


    public ContributionRequestSourceWriter(ProcessorElement processorElement, String presenterName,
                                           String targetPackage, String generatedClassName, String presenterMethod) {
        super(processorElement);
        this.targetPackage = targetPackage;
        this.generatedClassName = generatedClassName;
        this.presenterMethod = presenterMethod;
        presenterType = ClassName.bestGuess(presenterName);
        contextType = ClassName.bestGuess(getContextName());
    }

    private String getContextName() {
        return new FullClassName(processorElement.getInterfaceFullQualifiedGenericName(Contribution.class)).allImports().get(1);
    }

    @Override
    public String write() throws IOException {

        FieldSpec extensionPoint = makeExtensionPointField(contextType);
        MethodSpec constructor = makeConstructor(contextType);
        MethodSpec process = makeProcessMethod(presenterType);

        AnnotationSpec generatedAnnotation = AnnotationSpec.builder(Generated.class)
                .addMember("value", "\"" + ContributionClientRequestProcessor.class.getCanonicalName() + "\"")
                .build();

        TypeSpec contributionRequest = TypeSpec.classBuilder(generatedClassName)
                .addAnnotation(generatedAnnotation)
                .addAnnotation(Request.class)
                .addModifiers(Modifier.PUBLIC)
                .superclass(ParameterizedTypeName.get(ClassName.get(ClientRequest.class), presenterType))
                .addField(extensionPoint)
                .addMethod(constructor)
                .addMethod(process)
                .build();

        StringBuilder asString = new StringBuilder();
        JavaFile.builder(targetPackage, contributionRequest).build().writeTo(asString);
        return asString.toString();
    }

    private MethodSpec makeProcessMethod(ClassName presenterType) {
        return MethodSpec.methodBuilder("process")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .addParameter(presenterType, "presenter")
                .addStatement("presenter." + presenterMethod + "(extensionPoint.context())")
                .build();
    }

    private MethodSpec makeConstructor(ClassName contextType) {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(contextType, "extensionPoint")
                .addStatement("this.extensionPoint = extensionPoint")
                .build();
    }

    private FieldSpec makeExtensionPointField(ClassName contextType) {
        return FieldSpec.builder(contextType, "extensionPoint", Modifier.PRIVATE).build();
    }
}
