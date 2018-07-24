package org.dominokit.domino.apt.client.processors.inject;

import org.dominokit.domino.api.client.annotations.Listener;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;
import org.dominokit.domino.apt.commons.FullClassName;
import org.dominokit.domino.apt.commons.JavaSourceWriter;
import org.dominokit.domino.apt.commons.ProcessorElement;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;

public class DominoEventListenerSourceWriter extends JavaSourceWriter {

    private final String dominoEvent;
    private final String targetPackage;
    private final FullClassName presenterFullClassName;
    private final String className;


    public DominoEventListenerSourceWriter(ProcessorElement processorElement, String presenter,
                                           String dominoEvent, String targetPackage, String className) {
        super(processorElement);
        this.dominoEvent = dominoEvent;
        this.targetPackage = targetPackage;
        this.className = className;
        this.presenterFullClassName = new FullClassName(presenter);
    }

    @Override
    public String write() throws IOException {
        TypeSpec listenerType = DominoTypeBuilder.build(className, ListenToDominoEventProcessor.class)
                .addAnnotation(Listener.class)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(DominoEventListener.class), ClassName.bestGuess(dominoEvent)))
                .addMethod(makeListenMethod())
                .build();

        StringBuilder asString = new StringBuilder();
        JavaFile.builder(targetPackage, listenerType).skipJavaLangImports(true).build().writeTo(asString);
        return asString.toString();
    }

    private MethodSpec makeListenMethod() {
        return MethodSpec.methodBuilder("listen")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.bestGuess(dominoEvent), "event")
                .addStatement("new $T().onPresenterReady(presenter -> presenter.$L(event.context())).send()", ClassName.bestGuess(makeRequestClassName()), processorElement.simpleName())
                .build();
    }

    private String makeRequestClassName() {
        return presenterFullClassName.asPackage() + "." +
                presenterFullClassName.asSimpleName() +
                "Command";
    }
}
