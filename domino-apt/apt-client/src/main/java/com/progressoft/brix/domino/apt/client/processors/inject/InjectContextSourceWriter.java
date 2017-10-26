package com.progressoft.brix.domino.apt.client.processors.inject;

import com.progressoft.brix.domino.api.client.annotations.AutoRequest;
import com.progressoft.brix.domino.api.client.annotations.Contribute;
import com.progressoft.brix.domino.api.shared.extension.Contribution;
import com.progressoft.brix.domino.apt.commons.DominoTypeBuilder;
import com.progressoft.brix.domino.apt.commons.FullClassName;
import com.progressoft.brix.domino.apt.commons.JavaSourceWriter;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;

public class InjectContextSourceWriter extends JavaSourceWriter {

    private final String extensionPoint;
    private final String targetPackage;
    private final FullClassName presenterFullClassName;
    private final FullClassName extensionPointFullClassName;
    private final String className;


    public InjectContextSourceWriter(ProcessorElement processorElement, String presenter,
                                     String extensionPoint, String targetPackage, String className) {
        super(processorElement);
        this.extensionPoint = extensionPoint;
        this.targetPackage = targetPackage;
        this.className = className;
        this.presenterFullClassName = new FullClassName(presenter);
        this.extensionPointFullClassName = new FullClassName(extensionPoint);
    }

    @Override
    public String write() throws IOException {
        AnnotationSpec autoRequestAnnotation = AnnotationSpec.builder(AutoRequest.class)
                .addMember("presenters", "{$T.class}", ClassName.get(presenterFullClassName.asPackage(), presenterFullClassName.asSimpleName()))
                .addMember("method", "\"" + processorElement.simpleName() + "\"")
                .build();
        TypeSpec contributionType = DominoTypeBuilder.build(className, InjectContextProcessor.class)
                .addAnnotation(Contribute.class)
                .addAnnotation(autoRequestAnnotation)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(Contribution.class), ClassName.bestGuess(extensionPoint)))
                .addMethod(makeContributeMethod())
                .build();

        StringBuilder asString = new StringBuilder();
        JavaFile.builder(targetPackage, contributionType).build().writeTo(asString);
        return asString.toString();
    }

    private MethodSpec makeContributeMethod() {
        return MethodSpec.methodBuilder("contribute")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.bestGuess(extensionPoint), "extensionPoint")
                .addStatement("new $T().onPresenterReady(presenter -> presenter.$L(extensionPoint.context())).send()", ClassName.bestGuess(makeRequestClassName()), processorElement.simpleName())
                .build();
    }

    private String makeRequestClassName() {
        return targetPackage.replace("contributions", "requests") + "." +
                "Obtain" + extensionPointFullClassName.asSimpleName() +
                "For" + presenterFullClassName.asSimpleName() +
                "PresenterCommand";
    }
}
