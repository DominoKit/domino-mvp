package com.progressoft.brix.domino.apt.client.processors.contributions;

import com.progressoft.brix.domino.api.client.annotations.Command;
import com.progressoft.brix.domino.api.client.request.PresenterCommand;
import com.progressoft.brix.domino.apt.commons.DominoTypeBuilder;
import com.progressoft.brix.domino.apt.commons.JavaSourceWriter;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

public class ContributionRequestSourceWriter extends JavaSourceWriter {

    private final String targetPackage;
    private final String generatedClassName;
    private final ClassName presenterType;


    public ContributionRequestSourceWriter(ProcessorElement processorElement, String presenterName,
                                           String targetPackage, String generatedClassName) {
        super(processorElement);
        this.targetPackage = targetPackage;
        this.generatedClassName = generatedClassName;
        presenterType = ClassName.bestGuess(presenterName);
    }

    @Override
    public String write() throws IOException {

        TypeSpec contributionRequest = DominoTypeBuilder.build(generatedClassName, ContributionClientRequestProcessor.class)
                .addAnnotation(Command.class)
                .superclass(ParameterizedTypeName.get(ClassName.get(PresenterCommand.class), presenterType))
                .build();

        StringBuilder asString = new StringBuilder();
        JavaFile.builder(targetPackage, contributionRequest).skipJavaLangImports(true).build().writeTo(asString);
        return asString.toString();
    }

}
