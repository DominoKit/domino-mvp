package org.dominokit.domino.apt.client.processors.inject;

import org.dominokit.domino.api.client.annotations.Contribute;
import org.dominokit.domino.api.shared.extension.Contribution;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;
import org.dominokit.domino.apt.commons.FullClassName;
import org.dominokit.domino.apt.commons.JavaSourceWriter;
import org.dominokit.domino.apt.commons.ProcessorElement;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;

public class InjectContextSourceWriter extends JavaSourceWriter {

    private final String extensionPoint;
    private final String targetPackage;
    private final FullClassName presenterFullClassName;
    private final String className;


    public InjectContextSourceWriter(ProcessorElement processorElement, String presenter,
                                     String extensionPoint, String targetPackage, String className) {
        super(processorElement);
        this.extensionPoint = extensionPoint;
        this.targetPackage = targetPackage;
        this.className = className;
        this.presenterFullClassName = new FullClassName(presenter);
    }

    @Override
    public String write() throws IOException {
        TypeSpec contributionType = DominoTypeBuilder.build(className, InjectContextProcessor.class)
                .addAnnotation(Contribute.class)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(Contribution.class), ClassName.bestGuess(extensionPoint)))
                .addMethod(makeContributeMethod())
                .build();

        StringBuilder asString = new StringBuilder();
        JavaFile.builder(targetPackage, contributionType).skipJavaLangImports(true).build().writeTo(asString);
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
        return presenterFullClassName.asPackage() + "." +
                presenterFullClassName.asSimpleName() +
                "Command";
    }
}
