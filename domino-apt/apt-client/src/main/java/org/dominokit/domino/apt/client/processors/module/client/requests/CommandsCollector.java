package org.dominokit.domino.apt.client.processors.module.client.requests;

import org.dominokit.domino.api.client.annotations.Command;
import org.dominokit.domino.api.client.request.PresenterCommand;
import org.dominokit.domino.apt.commons.BaseProcessor;
import org.dominokit.domino.apt.commons.FullClassName;
import org.dominokit.domino.apt.commons.ProcessorElement;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandsCollector {

    private final Messager messager;
    private final Types typeUtils;
    private final BaseProcessor.ElementFactory elementFactory;
    private final Set<String> requests;

    public CommandsCollector(Messager messager, Types typeUtils,
                             BaseProcessor.ElementFactory elementFactory, Set<String> requests) {
        this.messager = messager;
        this.typeUtils = typeUtils;
        this.elementFactory = elementFactory;
        this.requests = requests;
    }

    public void collectCommands(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Command.class).stream()
                .map(elementFactory::make)
                .filter(e -> e.validateElementKind(ElementKind.CLASS))
                .collect(Collectors.toSet())
                .forEach(this::addRequest);
    }

    private boolean addRequest(ProcessorElement r) {
        if(!isRequestClass(r)){
            messager.printMessage(Diagnostic.Kind.ERROR, "Not extending base request class", r.getElement());
            throw new NotExtendBaseRequestClassException();
        }
        return requests.add(r.fullQualifiedNoneGenericName() + ":" +
                getRequestPresenter(r));
    }

    private boolean isRequestClass(ProcessorElement element) {
        String superType = elementFactory.make(typeUtils.asElement(element.asTypeElement().getSuperclass()))
                .fullQualifiedNoneGenericName();
        return superType.equals(PresenterCommand.class.getCanonicalName());
    }

    private String getRequestPresenter(ProcessorElement element) {
        return new FullClassName(typeUtils.capture(element.asTypeElement().getSuperclass()).toString()).allImports()
                .get(1);
    }

    public class NotExtendBaseRequestClassException extends RuntimeException {
    }
}