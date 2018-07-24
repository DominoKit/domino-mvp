package org.dominokit.domino.apt.client.processors.module.client.listeners;

import org.dominokit.domino.api.client.annotations.Listener;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.apt.commons.BaseProcessor;
import org.dominokit.domino.apt.commons.FullClassName;
import org.dominokit.domino.apt.commons.ProcessorElement;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.tools.Diagnostic;
import java.util.Set;
import java.util.stream.Collectors;

public class ListenersCollector {

    private final Messager messager;
    private final BaseProcessor.ElementFactory elementFactory;
    private final Set<String> listeners;

    public ListenersCollector(Messager messager,
                              BaseProcessor.ElementFactory elementFactory, Set<String> listeners) {
        this.messager = messager;
        this.elementFactory = elementFactory;
        this.listeners = listeners;
    }

    public void collectListeners(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Listener.class).stream()
                .map(elementFactory::make)
                .filter(e -> e.validateElementKind(ElementKind.CLASS))
                .collect(Collectors.toSet())
                .forEach(this::addListener);
    }

    private boolean addListener(ProcessorElement c) {
        if(!c.isImplementsGenericInterface(DominoEventListener.class)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Not implementing listener interface", c.getElement());
            throw new NotImplementingListenerInterfaceException();
        }
        return listeners
                .add(c.fullQualifiedNoneGenericName() + ":" +
                        getListenerDominoEvent(c));
    }

    private String getListenerDominoEvent(ProcessorElement element) {
        String listener = element.getInterfaceFullQualifiedGenericName(DominoEventListener.class);
        return new FullClassName(listener).allImports().get(1);
    }

    public class NotImplementingListenerInterfaceException extends RuntimeException {
    }
}