package org.dominokit.domino.apt.client.processors.module.client.presenters;

import com.squareup.javapoet.*;
import org.dominokit.domino.api.client.annotations.Command;
import org.dominokit.domino.api.client.request.PresenterCommand;
import org.dominokit.domino.apt.commons.AbstractSourceBuilder;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public class PresenterCommandSourceWriter extends AbstractSourceBuilder {
    private final Element presenterElement;

    public PresenterCommandSourceWriter(Element presenterElement, ProcessingEnvironment processingEnv) {
        super(processingEnv);
        this.presenterElement = presenterElement;
    }

    @Override
    public TypeSpec.Builder asTypeBuilder() {
        return DominoTypeBuilder.build(presenterElement.getSimpleName().toString() + "Command", PresenterProcessor.class)
                .addAnnotation(Command.class)
                .superclass(ParameterizedTypeName.get(ClassName.get(PresenterCommand.class), TypeName.get(presenterElement.asType())))
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("configure(new $T())", ClassName.bestGuess(elements.getPackageOf(presenterElement).getQualifiedName().toString()+"."+presenterElement.getSimpleName().toString()+"_Config"))
                        .build());
    }
}
