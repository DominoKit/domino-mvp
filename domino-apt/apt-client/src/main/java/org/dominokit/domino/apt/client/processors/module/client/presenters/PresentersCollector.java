package org.dominokit.domino.apt.client.processors.module.client.presenters;

import org.dominokit.domino.api.client.annotations.presenter.Presenter;
import org.dominokit.domino.api.client.mvp.presenter.Presentable;
import org.dominokit.domino.apt.commons.BaseProcessor;
import org.dominokit.domino.apt.commons.ProcessorElement;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Set;
import java.util.stream.Collectors;

public class PresentersCollector {

    private final Messager messager;
    private final Types typeUtils;
    private final Elements elementUtils;
    private final BaseProcessor.ElementFactory elementFactory;
    private final Set<String> presenters;


    public PresentersCollector(Messager messager, Types typeUtils, Elements elementUtils,
                               BaseProcessor.ElementFactory elementFactory, Set<String> presenters) {
        this.messager = messager;
        this.typeUtils = typeUtils;
        this.elementUtils = elementUtils;
        this.elementFactory = elementFactory;
        this.presenters = presenters;
    }

    public void collectPresenters(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Presenter.class).stream()
                .map(elementFactory::make)
                .filter(e -> e.validateElementKind(ElementKind.CLASS))
                .collect(Collectors.toSet())
                .forEach(this::addPresenter);
    }

    private boolean addPresenter(ProcessorElement p) {
        if(!p.isAssignableFrom(Presentable.class)){
            messager.printMessage(Diagnostic.Kind.ERROR, "Not implementing presentable interface", p.getElement());
            throw new NotImplementingPresentableInterfaceException();
        }
        return presenters.add(p.fullQualifiedNoneGenericName());
    }


    public class NotImplementingPresentableInterfaceException extends RuntimeException {
    }
}