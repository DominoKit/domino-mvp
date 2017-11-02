package com.progressoft.brix.domino.apt.client.processors.module.client.presenters;

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

public class PresenterCommandSourceWriter extends JavaSourceWriter {

    private final String targetPackage;
    private final String className;
    private final ClassName presenterType;


    public PresenterCommandSourceWriter(ProcessorElement processorElement, String targetPackage,
                                        String className, String presenterName) {
        super(processorElement);
        this.targetPackage = targetPackage;
        this.className = className;
        this.presenterType = ClassName.bestGuess(presenterName);
    }

    @Override
    public String write() throws IOException {
        TypeSpec contributionRequest = DominoTypeBuilder.build(className, PresenterCommandProcessor.class)
                .addAnnotation(Command.class)
                .superclass(ParameterizedTypeName.get(ClassName.get(PresenterCommand.class), presenterType))
                .build();

        StringBuilder asString = new StringBuilder();
        JavaFile.builder(targetPackage, contributionRequest).skipJavaLangImports(true).build().writeTo(asString);
        return asString.toString();
    }
}
